package com.ivanwidyan.springboot.model.request;

import com.ivanwidyan.springboot.entity.Member;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CreateMemberWebRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
