package com.ivanwidyan.springboot.plugin;

import com.ivanwidyan.springboot.entity.Member;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response<T> {
    private Integer code;
    private String status;
    private T data;

    public Response(T data) {
        this.code = HttpStatus.OK.value();
        this.status = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
