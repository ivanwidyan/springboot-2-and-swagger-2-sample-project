package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.BadRequestException;
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

    @ApiOperation(value = "Get All Members")
    @GetMapping(path = "/allmember", produces="application/json")
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @ApiOperation(value = "Get Member by Id")
    @GetMapping(path = "/members", produces="application/json")
    public ResponseEntity<Member> getMemberById(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {

        Member member = memberRepository.findById(id);
        if (member == null) throw new BadRequestException("id not found");
        idValidator(id);
        return ResponseEntity.ok().body(member);
    }

    @ApiOperation(value = "Create Member")
    @PostMapping(path = "/member", produces="application/json", consumes="application/json")
    public Member createMember(
            @ApiParam(required = true) @Valid @RequestBody Member memberDetails)
            throws Exception {

        idValidator(memberDetails.getId());
        Member member = memberRepository.findById(memberDetails.getId());
        if (member != null) throw new BadRequestException("id already exist");

        nameValidator(memberDetails.getName());
        emailValidator(memberDetails.getEmail());
        phoneNumberValidator(memberDetails.getPhoneNumber());

        return memberRepository.save(memberDetails);
    }

    @ApiOperation(value = "Update member")
    @PutMapping(path = "/member", produces="application/json", consumes="application/json")
    public ResponseEntity<Member> updateMember(
            @ApiParam(required = true) @RequestParam Integer id,
            @ApiParam(required = true) @Valid @RequestBody Member memberDetails)
            throws Exception {

        idValidator(id);
        Member existingMember = memberRepository.findById(id);
        if (existingMember == null) throw new BadRequestException("id not found");

        idValidator(memberDetails.getId());
        Member wannabeMember = memberRepository.findById(memberDetails.getId());
        if (wannabeMember != null) throw new BadRequestException("id already exist");

        nameValidator(memberDetails.getName());
        emailValidator(memberDetails.getEmail());
        phoneNumberValidator(memberDetails.getPhoneNumber());

        existingMember.setName(memberDetails.getName());
        existingMember.setEmail(memberDetails.getEmail());
        existingMember.setPhoneNumber(memberDetails.getPhoneNumber());
        final Member updatedMember = memberRepository.save(wannabeMember);
        return ResponseEntity.ok(updatedMember);
    }

    @ApiOperation(value = "Delete member")
    @DeleteMapping("/member")
    public Map<String, Boolean> deleteMember(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {

        idValidator(id);
        Member member = memberRepository.findById(id);
        if (member == null) throw new BadRequestException("id not found");

        memberRepository.delete(member);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return  response;
    }

    private void idValidator(Integer id) throws Exception {
        if (id == null)
            throw new BadRequestException("id is null");
        if (id > 999999) {
            throw new BadRequestException("id is too large");
        } else if (id < 1) {
            throw new BadRequestException("id is too small");
        }
    }

    private void nameValidator(String name) throws Exception {
        if (name.isEmpty())
            throw new BadRequestException("name is blank");
        else if (name == null)
            throw new BadRequestException("name is null");

        final String phoneRegex = "^[a-zA-Z ]*$";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (!pattern.matcher(name).matches())
            throw new BadRequestException("name format is invalid");
    }

    private void emailValidator(String email) throws Exception {
        if (email.isEmpty())
            throw new BadRequestException("email is blank");
        else if (email == null)
            throw new BadRequestException("email is null");

        final String emailRegex ="^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches())
            throw new BadRequestException("email format is invalid");

    }

    private void phoneNumberValidator(String phoneNumber) throws Exception {
        if (phoneNumber.isEmpty())
            throw new BadRequestException("phoneNumber is blank");
        else if (phoneNumber == null)
            throw new BadRequestException("phoneNumber is null");
        final String phoneRegex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(phoneRegex);

        if (!pattern.matcher(phoneNumber).matches())
            throw new BadRequestException("phone number format is invalid");
    }
}
