package com.br.totvs.service;

import com.br.totvs.exception.InvalidFieldException;
import com.br.totvs.model.Cliente;
import com.br.totvs.model.TelefoneCliente;
import com.br.totvs.repository.ClienteRepository;
import com.br.totvs.service.message.MessageService;
import com.br.totvs.util.Util;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService extends GenericServiceAbstract<Cliente,Long> {

    private final ClienteRepository clienteRepository;
    private final TelefoneClienteService telefoneClienteService;
    private final MessageService messageService;
    public ClienteService(ClienteRepository clienteRepository, TelefoneClienteService telefoneClienteService,MessageService messageService) {
        super(clienteRepository,messageService);
        this.clienteRepository = clienteRepository;
        this.telefoneClienteService = telefoneClienteService;
        this.messageService = messageService;
    }

    public Optional<Cliente> findByNome(String nome) {
        return Optional.ofNullable(clienteRepository.findByNome(nome).orElse(null));
    }

    @Override
    public void validateBeforeSave(Cliente cliente) throws InvalidFieldException {
        validateNome(cliente);
        validateTelefones(cliente);
    }

    @Override
    public void validateBeforeUpdate(Cliente cliente) throws InvalidFieldException {
        validateNome(cliente);
        validateTelefones(cliente);
    }

    /**
     * Método para verificar se existe nome duplicado
     * @param cliente entidade {@link Cliente}
     * @throws InvalidFieldException caso ocorra erro de validação
     */
    public void validateNome(Cliente cliente) throws InvalidFieldException {
        if (Strings.isEmpty(cliente.getNome())) {
            throw new InvalidFieldException(HttpStatus.BAD_REQUEST, messageService.getMessage("error.invalidName"));
        }

        if (cliente.getNome().length() <= 10) {
            throw new InvalidFieldException(HttpStatus.BAD_REQUEST, messageService.getMessage("error.invalidName.lengh"));
        }

        Optional<Cliente> existingCliente = clienteRepository.findByNome(cliente.getNome());
        if (existingCliente.isPresent() && !existingCliente.get().getId().equals(cliente.getId())) {
            throw new InvalidFieldException(HttpStatus.CONFLICT, messageService.getMessage("error.duplicateName"));
        }
    }

    /**
     * Método para verificar se existe telefone duplicado
     * @param cliente entidade {@link Cliente}
     * @throws InvalidFieldException caso ocorra erro de validação
     */
    public void validateTelefones(Cliente cliente) throws InvalidFieldException {
        if(cliente.getTelefoneClienteList()!=null){
            for (TelefoneCliente telefone : cliente.getTelefoneClienteList()) {
                if (Strings.isEmpty(telefone.getNumero()) || !Util.isValidPhoneFormat(telefone.getNumero())) {
                    throw new InvalidFieldException(HttpStatus.BAD_REQUEST, messageService.getMessage("error.invalidPhone.format"));
                }
                Optional<Cliente> existingCliente = telefoneClienteService.findClienteByNumero(telefone.getNumero());
                if (existingCliente.isPresent() && !existingCliente.get().getId().equals(cliente.getId())) {
                    throw new InvalidFieldException(HttpStatus.CONFLICT, messageService.getMessage("error.duplicatePhone"));
                }
            }
        }
    }
}
