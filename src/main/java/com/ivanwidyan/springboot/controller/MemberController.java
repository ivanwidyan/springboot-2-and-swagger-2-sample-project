package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.BadRequestException;
import com.ivanwidyan.springboot.entity.Member;
import com.ivanwidyan.springboot.exception.InternalServerErrorBody;
import com.ivanwidyan.springboot.model.request.CreateMemberWebRequest;
import com.ivanwidyan.springboot.model.response.BooleanWebResponse;
import com.ivanwidyan.springboot.model.response.GetListMemberWebResponse;
import com.ivanwidyan.springboot.model.response.GetMemberWebResponse;
import com.ivanwidyan.springboot.repository.MemberRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GetListMemberWebResponse> getAllMembers()
            throws Exception {
        return new ResponseEntity<>(new GetListMemberWebResponse(memberRepository.findAll()), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Member by Id")
    @GetMapping(path = "/members", produces="application/json")
    public ResponseEntity<GetMemberWebResponse> getMemberById(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {
        return new ResponseEntity<>(new GetMemberWebResponse(idValidator(id)), HttpStatus.OK);
    }

    @ApiOperation(value = "Create Member")
    @PostMapping(path = "/member", produces="application/json", consumes="application/json")
    public ResponseEntity<GetMemberWebResponse> createMember(
            @ApiParam(required = true) @Valid @RequestBody CreateMemberWebRequest request)
            throws Exception {

        nameValidator(request.getName());
        emailValidator(request.getEmail());
        phoneNumberValidator(request.getPhoneNumber());

        Member member = new Member(request.getName(), request.getEmail(), request.getPhoneNumber());
        return new ResponseEntity<>(new GetMemberWebResponse(memberRepository.save(member)), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Member")
    @PutMapping(path = "/member", produces="application/json", consumes="application/json")
    public ResponseEntity<GetMemberWebResponse> updateMember(
            @ApiParam(required = true) @RequestParam Integer id,
            @ApiParam(required = true) @Valid @RequestBody CreateMemberWebRequest request)
            throws Exception {

        Member member = idValidator(id);

        nameValidator(request.getName());
        emailValidator(request.getEmail());
        phoneNumberValidator(request.getPhoneNumber());

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhoneNumber(request.getPhoneNumber());
        return new ResponseEntity<>(new GetMemberWebResponse(memberRepository.save(member)), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Member")
    @DeleteMapping(path = "/member", produces="application/json")
    public ResponseEntity<BooleanWebResponse> deleteMember(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {

        memberRepository.delete(idValidator(id));
        return new ResponseEntity<>(new BooleanWebResponse(true), HttpStatus.OK);
    }

    private Member idValidator(Integer id) throws Exception {
        if (id == null)
            throw new BadRequestException("id is null");

        Member member = memberRepository.findById(id);
        if (memberRepository.findById(id) == null) throw new BadRequestException("id not found");
        return member;
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

        Member member = memberRepository.findByEmail(email);
        if (member != null) throw new BadRequestException("email already exist");
    }

    private void phoneNumberValidator(String phoneNumber) throws Exception {
        if (phoneNumber.isEmpty())
            throw new BadRequestException("phoneNumber is blank");
        else if (phoneNumber == null)
            throw new BadRequestException("phoneNumber is null");
        if (phoneNumber.length() > 13)
            throw new BadRequestException("phoneNumber is too long");

        final String phoneRegex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(phoneRegex);

        if (!pattern.matcher(phoneNumber).matches())
            throw new BadRequestException("phone number format is invalid");

        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member != null) throw new BadRequestException("phoneNumber already exist");
    }
}
