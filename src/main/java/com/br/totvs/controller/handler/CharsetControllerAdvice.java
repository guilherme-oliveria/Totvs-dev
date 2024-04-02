package com.br.totvs.controller.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CharsetControllerAdvice {

    @ModelAttribute
    public void setResponseCharacterEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
    }
}