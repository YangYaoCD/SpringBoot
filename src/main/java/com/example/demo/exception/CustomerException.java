package com.example.demo.exception;

public class CustomerException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomerException(ICustomizeErrorCode errorCode){
        this.code=errorCode.getCode();
        this.message=errorCode.getMessage();
    }

    public CustomerException(String s) {

    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
