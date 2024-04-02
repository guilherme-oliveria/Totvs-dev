package com.br.totvs.dto;

import com.br.totvs.dto.interfaces.EntityDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Classe para representação de telefone do cliente
 * para operações de api rest
 */
public record TelefoneClienteDTO(
        Long id,
        @NotNull(message = "notNull.telefone")
        @Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$", message = "pattern.telefone")
        String numero

) implements EntityDTO {
    @Override
    public Long getId() {
        return this.id;
    }
}