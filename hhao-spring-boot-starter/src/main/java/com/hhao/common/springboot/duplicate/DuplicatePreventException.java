package com.hhao.common.springboot.duplicate;

import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.ErrorCode;
import com.hhao.common.exception.error.request.RequestRuntimeException;

public class DuplicatePreventException extends RequestRuntimeException {
    public DuplicatePreventException(String message) {
        super(message);
    }

    public DuplicatePreventException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatePreventException(String message, Object [] args) {
        super(message, args);
    }

    public DuplicatePreventException(String message, Throwable cause, Object [] args) {
        super(message, cause, args);
    }

    public DuplicatePreventException(Throwable throwable){
        super(DefaultErrorCodes.ERROR_503,throwable);
    }

    public DuplicatePreventException(ErrorCode errorCode, Throwable throwable){
        super(errorCode,throwable);
    }
}
