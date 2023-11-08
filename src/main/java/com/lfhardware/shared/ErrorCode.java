package com.lfhardware.shared;

public enum ErrorCode {

    LOGIN("A0001");

    public final String errorCode;

    ErrorCode(String errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
