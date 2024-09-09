package com.sunghyun.football.global.exception.exceptions.file;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class FileException extends AppException {
    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
