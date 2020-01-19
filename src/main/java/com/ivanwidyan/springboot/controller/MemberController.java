package com.ivanwidyan.springboot.controller;

import com.ivanwidyan.springboot.exception.BadRequestException;
import com.ivanwidyan.springboot.entity.Member;
import com.ivanwidyan.springboot.model.request.CreateMemberWebRequest;
import com.ivanwidyan.springboot.plugin.Response;
import com.ivanwidyan.springboot.repository.MemberRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@Api(value = "Member management system")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @ApiOperation(value = "Get All Members")
    @GetMapping(path = "/members", produces="application/json")
    public Response<List<Member>> getAllMembers()
            throws Exception {
        return new Response<>(memberRepository.findAll());
    }

    @ApiOperation(value = "Get Member by Id")
    @GetMapping(path = "/member", produces="application/json")
    public Response<Member> getMemberById(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {
        return new Response<Member>(idValidator(id));
    }

    @ApiOperation(value = "Create Member")
    @PostMapping(path = "/member", produces="application/json", consumes="application/json")
    public Response<Member> createMember(
            @ApiParam(required = true) @Valid @RequestBody CreateMemberWebRequest request)
            throws Exception {

        nameValidator(request.getName());
        emailValidator(request.getEmail());
        phoneNumberValidator(request.getPhoneNumber());

        Member member = new Member(request.getName(), request.getEmail(), request.getPhoneNumber());
        return new Response<Member>(memberRepository.save(member));
    }

    @ApiOperation(value = "Update Member")
    @PutMapping(path = "/member", produces="application/json", consumes="application/json")
    public Response<Member> updateMember(
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
        return new Response<Member>(memberRepository.save(member));
    }

    @ApiOperation(value = "Delete Member")
    @DeleteMapping(path = "/member", produces="application/json")
    public Response<Boolean> deleteMember(
            @ApiParam(required = true) @RequestParam Integer id)
            throws Exception {

        memberRepository.delete(idValidator(id));
        return new Response<>(true);
    }

    private Member idValidator(Integer id) throws Exception {
        if (id == null)
            throw new BadRequestException("id is null");

        Member member = memberRepository.findById(id);
        if (memberRepository.findById(id) == null) throw new BadRequestException("id not found");
        return member;
    }

    private void nameValidator(String name) throws Exception {
        if (name == null)
            throw new BadRequestException("name is null");
        else if (name.isEmpty())
            throw new BadRequestException("name is blank");
        else if (name.length() > 100)
            throw new BadRequestException("name is too long");

        final String phoneRegex = "^[a-zA-Z ]*$";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (!pattern.matcher(name).matches())
            throw new BadRequestException("name format is invalid");
    }

    private void emailValidator(String email) throws Exception {
        if (email == null)
            throw new BadRequestException("email is null");
        else if (email.isEmpty())
            throw new BadRequestException("email is blank");

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
        if (phoneNumber == null)
            throw new BadRequestException("phoneNumber is null");
        else if (phoneNumber.isEmpty())
            throw new BadRequestException("phoneNumber is blank");
        else if (phoneNumber.length() > 13)
            throw new BadRequestException("phoneNumber is too long");

        final String phoneRegex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(phoneRegex);

        if (!pattern.matcher(phoneNumber).matches())
            throw new BadRequestException("phone number format is invalid");

        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member != null) throw new BadRequestException("phoneNumber already exist");
    }
}
