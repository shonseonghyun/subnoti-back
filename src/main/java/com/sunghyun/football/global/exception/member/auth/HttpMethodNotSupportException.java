package com.sunghyun.football.global.exception.member.auth;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class HttpMethodNotSupportException extends AuthenticationException {

    public HttpMethodNotSupportException(String code){
        super(code);
    }
}
