package com.project.management.constants;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecurityConstant {
    public static final Key JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public final static Long JWT_EXPIRE_TIME = 604800000L; // 1 week
}
