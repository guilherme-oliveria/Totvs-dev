package com.br.totvs.builders;

import com.br.totvs.repository.ClienteRepository;
import com.br.totvs.service.ClienteService;
import com.br.totvs.service.TelefoneClienteService;
import com.br.totvs.service.message.MessageService;

import static org.mockito.Mockito.mock;

public class BuilderClienteService {

    public ClienteService get(){
        ClienteService clienteService = new ClienteService(mock(ClienteRepository.class),mock(TelefoneClienteService.class),mock(MessageService.class));
        return clienteService;
    }
}
