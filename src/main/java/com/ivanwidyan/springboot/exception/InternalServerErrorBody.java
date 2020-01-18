package com.ivanwidyan.springboot.exception;

public class InternalServerErrorBody {
    private Integer code;
    private String status;

    public InternalServerErrorBody(Integer code, String status) {
        super();
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

}
