package com.br.totvs.mapper;

import com.br.totvs.dto.ClienteDTO;
import com.br.totvs.model.Cliente;
import org.mapstruct.Mapper;

/**
 * Mapper para cliente
 * para conversão de DTO para entidade e vice-versa
 * {@link Mapper}
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<Cliente, ClienteDTO> {
}