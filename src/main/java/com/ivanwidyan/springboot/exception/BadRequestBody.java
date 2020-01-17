package com.ivanwidyan.springboot.exception;

public class BadRequestBody {
    private Integer code;
    private String status;
    private String errors;

    public BadRequestBody(Integer code, String status, String errors) {
        super();
        this.code = code;
        this.status = status;
        this.errors = errors;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getErrors() {
        return errors;
    }

}
