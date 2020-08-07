package com.ardia.backend.security;

public class SecurityConstants {
    public static final String SECRET ="Secret";
    public static final long EXPERATION_TIME=864_000_000; //10days : 10 * (60 * 60 * 24) * 1000
//    public static final long EXPERATION_TIME=6_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
