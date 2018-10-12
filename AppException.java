package com.objectfrontier.training.java.jdbc;

import java.util.ArrayList;
import java.util.List;

public class AppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;
    private List<AppException> errorList = new ArrayList<>();

    public AppException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public AppException(List<AppException> errorList) {

        this.errorList = errorList;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<AppException> getErrorList() {
        return errorList;
    }
    public void setErrorList(List<AppException> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        return String.format(" errorCode=%s, message=%s ", errorCode, message);
    }
}
