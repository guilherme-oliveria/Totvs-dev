package com.br.totvs.service;

import com.br.totvs.model.Cliente;
import com.br.totvs.model.TelefoneCliente;
import com.br.totvs.repository.TelefoneClienteRepository;
import com.br.totvs.service.message.MessageService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviço para telefone do cliente
 * para operações de banco de dados
 * {@link Service}
 */
@Service
public class TelefoneClienteService extends GenericServiceAbstract<TelefoneCliente,Long> {

    private final TelefoneClienteRepository repository;

    public TelefoneClienteService(TelefoneClienteRepository repository, MessageService messageService) {
        super(repository,messageService);
        this.repository = repository;
    }
    @Override
    public void validateBeforeSave(TelefoneCliente entity) {

    }

    @Override
    public void validateBeforeUpdate(TelefoneCliente entity) {

    }

    public Optional<TelefoneCliente> findByNumero(String numero) {
        return repository.findByNumero(numero);
    }

    public Optional<Cliente> findClienteByNumero(String numero) {
        return repository.findClienteByNumero(numero);
    }
}
