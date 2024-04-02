package com.br.totvs.controller;

import com.br.totvs.dto.interfaces.EntityDTO;
import com.br.totvs.mapper.EntityMapper;
import com.br.totvs.service.GenericServiceAbstract;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Classe abstrata para operações de CRUD
 * @param <T> entidade
 * @param <DTO> dto
 * @param <PK> tipo do identificador
 * @param <SERVICE> serviço
 * @param <MAPPER> mapeador
 */
@Controller
public abstract class AbstractController<T extends Entity,
        DTO extends EntityDTO, PK, SERVICE extends GenericServiceAbstract<T , PK>,
        MAPPER extends EntityMapper<T , DTO>> {

    protected final SERVICE service;

    protected final MAPPER mapper;

    public AbstractController(SERVICE service, MAPPER mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /** Método para criar um registro
     * @param dto registro a ser criado
     * @return registro criado
     */
    @PostMapping
    public ResponseEntity<DTO> create(@Valid @RequestBody DTO dto) {
        T entity = service.save(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDto(entity), HttpStatus.CREATED);
    }

    /**
     * Método para atualizar um registro
     * @param dto registro a ser atualizado
     * @return registro atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@Valid @RequestBody DTO dto) {
        T entity = service.update(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    /**
     * Método para deletar um registro por id
     * @param id identificador do registro
     * @return status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable PK id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para buscar um registro por id
     * @param id identificador do registro
     * @return registro
     */
    @GetMapping("/{id}")
    public ResponseEntity<DTO> findById(@PathVariable PK id) {
        Optional<T> entityOptional = service.findById(id);
        if (entityOptional.isPresent()) {
            DTO dto = mapper.toDto(entityOptional.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método para buscar todos os registros
     * @return lista de registros
     */
    @GetMapping
    public ResponseEntity<List<DTO>> findAll() {
        List<T> entities = service.findAll();
        return ResponseEntity.ok(mapper.toDtos(entities));
    }
}
