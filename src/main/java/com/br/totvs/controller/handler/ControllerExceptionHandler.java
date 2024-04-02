package com.br.totvs.controller.handler;

import com.br.totvs.dto.ErrorResponseDTO;
import com.br.totvs.exception.CustomException;
import com.br.totvs.exception.InvalidFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe para tratamento de exceções
 * {@link ControllerAdvice}
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Método para tratar exceções customizadas
     * @param ex exceção
     * @return {@link ResponseEntity} com a mensagem de erro
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex) {
        HttpStatus errorCode = ex.getErrorCode();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(LocalDateTime.now(), errorCode.value(), errorCode.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.value()));
    }

    /**
     * Método para tratar exceções de campos inválidos
     * @param ex exceção
     * @return {@link ResponseEntity} com a mensagem de erro
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation Error", errors.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Método para tratar exceções de campos inválidos
     * @param ex exceção
     * @return {@link ResponseEntity} com a mensagem de erro
     */
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ErrorResponseDTO> handleCampoInvalidoException(InvalidFieldException ex) {
        HttpStatus errorCode = ex.getErrorCode();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(LocalDateTime.now(), errorCode.value(), errorCode.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.value()));
    }

    /**
     * Método para tratar exceções genéricas
     * @param ex exceção
     * @return {@link ResponseEntity} com a mensagem de erro
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getClass().getSimpleName(), ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}