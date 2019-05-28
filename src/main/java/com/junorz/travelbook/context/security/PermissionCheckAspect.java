package com.junorz.travelbook.context.security;

import java.util.function.Supplier;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.exception.InvalidOperationException;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.domain.Detail;
import com.junorz.travelbook.domain.Member;

/**
 * User may use a valid token to access other travelbook's resource.<br>
 * To prevent this situation, this Aspect check if the resource which being
 * modified associated with travelbook's id in token.
 */
@Component
@Aspect
public class PermissionCheckAspect {

    private final Repository rep;

    public PermissionCheckAspect(Repository rep) {
        this.rep = rep;
    }

    private String getAuthorizedId() {
        String travelBookId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Strings.isNullOrEmpty(travelBookId)) {
            invalid();
        }
        return travelBookId;
    }

    private void invalid() {
        throw new InvalidOperationException(Messages.INVALID_OPERATION);
    }

    private Supplier<InvalidOperationException> supplyInvalid() {
        return new Supplier<InvalidOperationException>() {
            @Override
            public InvalidOperationException get() {
                return new InvalidOperationException(Messages.INVALID_OPERATION);
            }
        };
    }

    // ----------- Member Controller
    @Before("execution(* com.junorz..*.MemberController.edit(..)) && args(id,..)")
    public void checkMemberEdit(String id) {
        Member member = Member.findById(id, rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(member.getTravelBook().getId()))
            invalid();
    }

    @Before("execution(* com.junorz..*.MemberController.delete(..)) && args(id,..)")
    public void checkMemberDelete(String id) {
        Member member = Member.findById(id, rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(member.getTravelBook().getId()))
            invalid();
    }

    // ----------- Detail Controller

    @Before("execution(* com.junorz..*.DetailController.create(..)) && args(dto,..)")
    public void checkDetailCreate(DetailCreateDto dto) {
        Member member = Member.findById(dto.getMemberId(), rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(member.getTravelBook().getId()))
            invalid();
    }

    @Before("execution(* com.junorz..*.DetailController.edit(..)) && args(id,dto,..)")
    public void checkDetailEdit(String id, DetailCreateDto dto) {
        Detail detail = Detail.findById(id, rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(detail.getTravelBook().getId()))
            invalid();

        Member member = Member.findById(dto.getMemberId(), rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(member.getTravelBook().getId()))
            invalid();
    }

    @Before("execution(* com.junorz..*.DetailController.delete(..)) && args(id,..)")
    public void checkDetailDelete(String id) {
        Detail detail = Detail.findById(id, rep).orElseThrow(supplyInvalid());
        if (!getAuthorizedId().equals(detail.getTravelBook().getId()))
            invalid();
    }

}
