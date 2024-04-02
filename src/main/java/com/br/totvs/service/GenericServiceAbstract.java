package com.br.totvs.service;

import com.br.totvs.exception.CustomException;
import com.br.totvs.model.interfaces.ChildEntity;
import com.br.totvs.service.message.MessageService;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Esta classe abstrata fornece uma implementação genérica de um serviço.
 * Ela inclui métodos para salvar, deletar, encontrar por id, encontrar todos, e obter o id de uma entidade.
 * Ela também inclui métodos abstratos para validar uma entidade antes de salvar ou atualizar, e um método para preparar uma entidade para atualização.
 *
 * @param <T>  o tipo de entidade que este serviço irá manipular
 * @param <ID> o tipo do id da entidade
 */
@Service
public abstract class GenericServiceAbstract<T extends Entity, ID> {

    private final JpaRepository<T, ID> repository;
    private final Class<T> entityClass;
    private final MessageService messageService;

    /**
     * Constrói um novo GenericService com o repositório fornecido.
     *
     * @param repository o repositório a ser usado para operações CRUD
     */
    @SuppressWarnings("unchecked")
    public GenericServiceAbstract(JpaRepository<T, ID> repository, MessageService messageService) {
        this.repository = repository;
        this.messageService = messageService;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Salva a entidade fornecida.
     *
     * @param entity a entidade a ser salva
     * @return a entidade salva
     */

    public T save(T entity) {
        configParentReferencesAuto(entity);
        validateBeforeSave(entity);
        return salvar(entity);
    }

    /**
     * Atualiza a entidade fornecida.
     *
     * @param entity a entidade a ser atualizada
     * @return a entidade atualizada
     */

    public T update(T entity){
        ID id = getId(entity);
        if (!repository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, messageService.getMessage("error.objectNotFound",entityClass.getSimpleName(),id));
        }
        configParentReferencesAuto(entity);
        validateBeforeUpdate(entity);
        return salvar(prepareForUpdate(id, entity));
    }

    /**
     * Salva a entidade fornecida.
     *
     * @param entity a entidade a ser salva
     * @return a entidade salva
     */
    @Transactional
    protected T salvar(T entity) {
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            String mensagemErro = messageService.getMessage("error.dataIntegrityViolation",entityClass.getSimpleName());
            throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, mensagemErro, e);
        } catch (Exception e) {
            String mensagemErro = messageService.getMessage(
                    "error.unexpectedError",
                    new Object[]{e.getMessage()}
            );
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, mensagemErro, e);
        }
    }

    /**
     * Exclui a entidade com o id fornecido.
     *
     * @param id o id da entidade a ser excluída
     */
    @Transactional
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, messageService.getMessage("error.objectNotFound",entityClass.getSimpleName(),id));
        }
        repository.deleteById(id);
    }

    /**
     * Recupera a entidade com o id fornecido.
     *
     * @param id o id da entidade a ser recuperada
     * @return a entidade recuperada
     */
    public Optional<T> findById(ID id) {
        if (!repository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, messageService.getMessage("error.objectNotFound",entityClass.getSimpleName(),id));
        }
        return repository.findById(id);
    }

    /**
     * Recupera todas as entidades.
     *
     * @return uma lista de todas as entidades
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Recupera o id da entidade fornecida.
     *
     * @param entity a entidade cujo id deve ser recuperado
     * @return o id da entidade
     */
    @SuppressWarnings("unchecked")
    public <T, ID> ID getId(T entity) {
        if (entity == null) {
            return null;
        }

        Class<?> clazz = entity.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        return (ID) field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage("error.illegalAccess", e.getMessage()));
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage("error.idFieldNotFound", entity.getClass().getName()));
    }

    /**
     * Configura as referências dos pais automaticamente.
     *
     * @param entity a entidade cujas referências dos pais devem ser configuradas
     */
    public void configParentReferencesAuto(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> collectionClass = (Class<?>) genericType.getActualTypeArguments()[0];
                if (ChildEntity.class.isAssignableFrom(collectionClass)) {
                    field.setAccessible(true);
                    try {
                        Collection<?> children = (Collection<?>) field.get(entity);
                        if (children != null) {
                            children.forEach(child -> {
                                ((ChildEntity) child).setParent(entity);
                            });
                        }
                    } catch (IllegalAccessException e) {
                        throw new CustomException(HttpStatus.NOT_FOUND, messageService.getMessage("error.accessFieldFailure",new Object[]{field.getName(), e.getMessage()}));
                    }
                }
            }
        }
    }

    /**
     * Valida a entidade fornecida antes de salvar.
     *
     * @param entity a entidade a ser validada
     */
    public abstract void validateBeforeSave(T entity);


    /**
     * Valida a entidade fornecida antes de atualizar.
     *
     * @param entity a entidade a ser validada
     */
    public abstract void validateBeforeUpdate(T entity);


    /**
     * Prepara a entidade fornecida para atualização.
     *
     * @param id     o id da entidade a ser atualizada
     * @param entity a entidade a ser atualizada
     * @return a entidade pronta para atualização
     */
    public T prepareForUpdate(ID id, T entity) {
        return entity;
    }
}