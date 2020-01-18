package com.ivanwidyan.springboot.model.response;

import com.ivanwidyan.springboot.entity.Member;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetListMemberWebResponse {
    private Integer code;
    private String status;
    private List<Member> data;

    public GetListMemberWebResponse(List<Member> data) {
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

    public List<Member> getData() {
        return data;
    }
}
