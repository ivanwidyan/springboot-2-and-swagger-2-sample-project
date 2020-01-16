package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.ResourceNotFoundException;
import com.ivanwidyan.springboot.model.Employee;
import com.ivanwidyan.springboot.model.Member;
import com.ivanwidyan.springboot.repository.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "Member management system")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "Get all employees data")
    @GetMapping("/get/allmember")
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @ApiOperation(value = "Get a member by Id")
    @GetMapping("/get/member")
    public ResponseEntity<Member> getMemberById(
            @ApiParam(value = "Member id from which member object will retrieve", required = true) @RequestParam Long id)
            throws ResourceNotFoundException {

        Member employee = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        return ResponseEntity.ok().body(employee);
    }
}
