package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.ResourceNotFoundException;
import com.ivanwidyan.springboot.model.Member;
import com.ivanwidyan.springboot.repository.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id :: " + id));
        return ResponseEntity.ok().body(employee);
    }

    @ApiOperation(value = "Add a member")
    @PostMapping("/register/member")
    public Member createMember(
            @ApiParam(value = "Save member data to the database", required = true) @Valid @RequestBody Member member) {
        return memberRepository.save(member);
    }

    @ApiOperation(value = "Update a member")
    @PutMapping("/update/member")
    public ResponseEntity<Member> updateMember(
            @ApiParam(value = "Member Id used to update specific member", required = true) @RequestParam Long id,
            @ApiParam(value = "Data for the updated member", required = true) @Valid @RequestBody Member memberDetails)
            throws ResourceNotFoundException {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id: " + id));
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhoneNumber(memberDetails.getPhoneNumber());
        final Member updatedMember = memberRepository.save(member);
        return ResponseEntity.ok(member);
    }

    @ApiOperation(value = "Delete a member")
    @DeleteMapping("/delete/member")
    public Map<String, Boolean> deleteMember(
            @ApiParam(value = "Member id used to delete specific member", required = true) @RequestParam Long id)
            throws ResourceNotFoundException {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id: " + id));
        memberRepository.delete(member);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return  response;
    }

}
