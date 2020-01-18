package com.ivanwidyan.springboot.model.response;

import com.ivanwidyan.springboot.entity.Member;
import org.springframework.http.HttpStatus;

public class BooleanWebResponse {
    private Integer code;
    private String status;
    private Boolean data;

    public BooleanWebResponse(Boolean data) {
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

    public Boolean getData() {
        return data;
    }
}
