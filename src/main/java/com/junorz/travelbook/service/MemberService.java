package com.junorz.travelbook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.audit.AuditHandler;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.Member;
import com.junorz.travelbook.utils.MessageUtil;

@Service
public class MemberService {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    
    private final Repository rep;
    private final PlatformTransactionManager txm;
    private final AuditHandler audit;
    
    public MemberService(Repository rep, PlatformTransactionManager txm, AuditHandler audit) {
        this.rep = rep;
        this.txm = txm;
        this.audit = audit;
    }
    
    public Member create(MemberCreateDto dto) {
        return audit.audit(Messages.MEMBER_CREATE_START, () -> {
            Member member = TxManager.of(txm).tx(() -> Member.create(dto, rep)).orElse(null);
            logger.info(MessageUtil.getMessage(Messages.LOG_MEMBER_CREATE_SUCCESS), member.getId(), member.getName(),
                    dto.getTravelBookId());
            return member;
        });
    }
    
    public Member edit(String memberId, MemberCreateDto dto) {
        return TxManager.of(txm).tx(() -> Member.edit(memberId, dto, rep)).orElse(null);
    }
    
    public Member delete(String memberId) {
        return audit.audit(() -> {
           return TxManager.of(txm).tx(() -> Member.delete(memberId, rep)).orElse(null);
        });
    }

}
