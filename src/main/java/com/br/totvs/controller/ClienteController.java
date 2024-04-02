package com.br.totvs.controller;

import com.br.totvs.dto.ClienteDTO;
import com.br.totvs.mapper.ClienteMapper;
import com.br.totvs.model.Cliente;
import com.br.totvs.service.ClienteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para cliente
 * para operações de banco de dados
 * {@link RestController}
 */
@RestController
@RequestMapping("/api/v1/clientes")
@Validated
public class ClienteController extends AbstractController<Cliente, ClienteDTO, Long, ClienteService, ClienteMapper> {

    public ClienteController(ClienteService service, ClienteMapper mapper) {
        super(service, mapper);
    }

}