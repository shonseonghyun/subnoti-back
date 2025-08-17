package com.sunghyun.football.global.exception.file;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class FileException extends AppException {
    public FileException(ErrorType errorType) {
        super(errorType);
    }
}
