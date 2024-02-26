package com.iflytek.rpa.registryServer.conf;

import com.iflytek.rpa.starter.utils.response.ErrorCodeEnum;

public class RegistryException extends Exception{
    private String code;
    private String message;

    public RegistryException() {
    }

    public RegistryException(ErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getFlag();
    }

    public RegistryException(String message) {
        this.message = message;
    }

    public RegistryException(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
