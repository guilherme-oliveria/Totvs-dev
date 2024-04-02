package com.br.totvs.dto;

import com.br.totvs.dto.interfaces.EntityDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * Classe para representação de cliente
 * para operações de api rest
 */
public record ClienteDTO(
        Long id,
        @NotNull(message = "notNull.nome")
        @Length(min = 10, message = "error.invalidName.lengh")
        String nome,
        String endereco,
        String bairro,
        @JsonManagedReference
        @Valid
        List<TelefoneClienteDTO> telefoneClienteList
) implements EntityDTO {
    @Override
    public Long getId() {
        return this.id;
    }

}