package com.junorz.travelbook.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.dto.MemberDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.context.validator.ValidateType;
import com.junorz.travelbook.context.validator.Validator;
import com.junorz.travelbook.domain.Member;
import com.junorz.travelbook.service.MemberService;
import com.junorz.travelbook.utils.ControllerUtil;
import com.junorz.travelbook.utils.MessageUtil;

@RestController
@RequestMapping("/api/travelbooks/members")
@CrossOrigin("*")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final Response response;

    public MemberController(MemberService memberService, Response response) {
        this.memberService = memberService;
        this.response = response;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody MemberCreateDto dto) {
        MemberDto data = MemberDto.of(memberService.create(dto));

        logger.info(MessageUtil.getMessage(Messages.LOG_MEMBER_CREATE_SUCCESS), data.getId(), data.getName(),
                dto.getTravelBookId());

        return ControllerUtil.ok(response.of(data, Messages.MEMBER_CREATE_SUCCESS));
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Response> edit(@PathVariable("id") String id, @Valid @RequestBody MemberCreateDto dto) {
        Member member = memberService.edit(id, dto);
        Validator.validate(ValidateType.REQUIRED, member, Messages.MEMBER_EDIT_FAILED);
        MemberDto data = MemberDto.of(member);
        return ControllerUtil.ok(response.of(data, Messages.MEMBER_EDIT_SUCCESS));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Response> delete(@PathVariable("id") String id, @RequestBody Map<String, String> params) {
        String travelBookId = params.get("travelBookId");
        if (Strings.isNullOrEmpty(travelBookId) || Strings.isNullOrEmpty(id)) {
            return ControllerUtil.badRequest(response.of(null, Messages.MEMBER_DELETE_FAILED, Status.FAILED));
        }
        Member member = memberService.delete(id);
        Validator.validate(ValidateType.REQUIRED, member, Messages.MEMBER_DELETE_FAILED);

        MemberDto data = MemberDto.of(member);
        return ResponseEntity.status(HttpStatus.OK).body(response.of(data, Messages.MEMBER_DELETE_SUCCESS));
    }

}
