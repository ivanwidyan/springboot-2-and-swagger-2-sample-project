package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.IncorrectFieldException;
import com.ivanwidyan.springboot.exception.ResourceNotFoundException;
import com.ivanwidyan.springboot.model.Member;
import com.ivanwidyan.springboot.repository.MemberRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@Api(value = "Member management system")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "Get all member data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Incorrect input on the data being send")
    })
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
            @ApiParam(value = "Save member data to the database", required = true) @Valid @RequestBody Member memberDetails)
        throws Exception{

        Member member = memberRepository.findById(memberDetails.getId()).orElse(null);

        if(member != null)
            throw new IncorrectFieldException("Member with id "+ memberDetails.getId() + " already exist");

        fieldValidator(memberDetails);

        return memberRepository.save(memberDetails);
    }

    @ApiOperation(value = "Update a member")
    @PutMapping("/update/member")
    public ResponseEntity<Member> updateMember(
            @ApiParam(value = "Member Id used to update specific member", required = true) @RequestParam Long id,
            @ApiParam(value = "Data for the updated member", required = true) @Valid @RequestBody Member memberDetails)
            throws Exception {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id: " + id));

        fieldValidator(memberDetails);

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

    private void fieldValidator(Member memberDetails) throws Exception {
        if(memberDetails.getName().isEmpty())
            throw new IncorrectFieldException("Name cannot be empty");
        else if(memberDetails.getEmail().isEmpty())
            throw new IncorrectFieldException("Name cannot be empty");
        else if(memberDetails.getPhoneNumber().isEmpty())
            throw new IncorrectFieldException("Name cannot be empty");

        final String emailRegex ="^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        if(!pattern.matcher(memberDetails.getEmail()).matches())
            throw new IncorrectFieldException("Email format is invalid: " + memberDetails.getEmail());

        final String phoneRegex = "^[a-zA-Z]*$";

        pattern = Pattern.compile(phoneRegex);

        if(pattern.matcher(memberDetails.getPhoneNumber()).matches())
            throw new IncorrectFieldException("Phone Number must not contains alphabet: " + memberDetails.getPhoneNumber());
    }

}
