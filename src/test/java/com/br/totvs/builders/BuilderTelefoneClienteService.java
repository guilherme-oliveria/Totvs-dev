package com.br.totvs.builders;

import com.br.totvs.repository.TelefoneClienteRepository;
import com.br.totvs.service.TelefoneClienteService;
import com.br.totvs.service.message.MessageService;

import static org.mockito.Mockito.mock;

public class BuilderTelefoneClienteService {

    public TelefoneClienteService get(){
        TelefoneClienteService telefoneClienteService = new TelefoneClienteService(mock(TelefoneClienteRepository.class),mock(MessageService.class));
        return telefoneClienteService;
    }
}
