package com.sunghyun.football.global.exception.file.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.file.FileException;

public class FileUploadFailException extends FileException
{
    public FileUploadFailException(ErrorType errorType) {
        super(errorType);
    }
}
