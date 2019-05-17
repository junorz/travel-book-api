package com.junorz.travelbook.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.dto.MemberDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.service.MemberService;

@RestController
@RequestMapping("/travelbooks/members")
@CrossOrigin("*")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource messageSource;

    public MemberController(MemberService memberService, MessageSource messageSource) {
        this.memberService = memberService;
        this.messageSource = messageSource;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody MemberCreateDto dto) {
        MemberDto data = MemberDto.of(memberService.create(dto.getTravelBookId(), dto.getMemberName()));
        String message = messageSource.getMessage(Messages.MEMBER_CREATE_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }

}
