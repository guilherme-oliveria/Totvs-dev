package com.br.totvs.util;

import java.util.regex.Pattern;

/**
 * Classe utilitária para validações
 */
public class Util {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$");

    public static boolean isValidPhoneFormat(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
