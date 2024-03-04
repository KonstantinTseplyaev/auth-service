package com.aston.hw4.authservice.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptEncoderUtil {
    private static final String salt = BCrypt.gensalt();

    public static String encode(String password) {
        return BCrypt.hashpw(password, salt);
    }
}
