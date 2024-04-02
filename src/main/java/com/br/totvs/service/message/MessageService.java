package com.br.totvs.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Properties;

/**
 * Serviço para mensagens.
 *
 * @author guilherme-oliveria
 */
@Service
public class MessageService {

    private final MessageSource messageSource;

    @Autowired
    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Obtém uma mensagem.
     *
     * @param messageKey a chave da mensagem
     * @param args argumentos da mensagem
     * @return a mensagem
     */
    public String getMessage(String messageKey, Object... args) {
        return messageSource.getMessage(messageKey, args, new Locale("pt", "BR"));
    }
}
