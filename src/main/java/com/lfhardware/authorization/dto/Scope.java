package com.lfhardware.authorization.dto;

public enum Scope {
    FORM_READ("form:read"), FORM_WRITE("form:write");

    private String scope;
    Scope(String scope){
        this.scope = scope;
    }

    public String getScope(){
        return this.scope;
    }
}
