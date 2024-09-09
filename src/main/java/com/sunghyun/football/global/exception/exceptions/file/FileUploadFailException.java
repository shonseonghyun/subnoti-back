package com.sunghyun.football.global.exception.exceptions.file;

import com.sunghyun.football.global.exception.ErrorCode;

public class FileUploadFailException extends FileException{
    public FileUploadFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
