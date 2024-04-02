package com.br.totvs.dto;

import java.time.LocalDateTime;

/**
 * Classe que representa o objeto de resposta de erro.
 */
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}