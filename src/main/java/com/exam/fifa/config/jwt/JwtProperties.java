package com.exam.fifa.config.jwt;

public interface JwtProperties {
    String SECRET = "SeonSo";
    Integer EXPIRATION_TIME = 1000*60*60*24*1000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
