package com.junorz.travelbook.controller;

import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Response> delete(@PathVariable("id") String id, @RequestBody Map<String, String> params) {
        String travelBookId = params.get("travelBookId");
        String memberId = params.get("memberId");
        String message = null;
        if (StringUtils.isEmpty(travelBookId) || StringUtils.isEmpty(memberId) || !id.equals(memberId)) {
            message = messageSource.getMessage(Messages.MEMBER_DELETE_FAILED, null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.OK).body(Response.of(null, message, Status.FAILED));
        }
        MemberDto data = MemberDto.of(memberService.delete(travelBookId, memberId));
        message = messageSource.getMessage(Messages.MEMBER_DELETE_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }

}
