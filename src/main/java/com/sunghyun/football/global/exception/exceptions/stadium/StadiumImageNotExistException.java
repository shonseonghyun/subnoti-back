package com.sunghyun.football.global.exception.exceptions.stadium;


import com.sunghyun.football.global.exception.ErrorCode;

public class StadiumImageNotExistException extends StadiumException{
    public StadiumImageNotExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}