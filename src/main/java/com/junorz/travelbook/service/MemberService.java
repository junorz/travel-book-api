package com.junorz.travelbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.Member;

@Service
public class MemberService {
    
    private final Repository rep;
    private final PlatformTransactionManager txm;
    
    public MemberService(Repository rep, PlatformTransactionManager txm) {
        this.rep = rep;
        this.txm = txm;
    }
    
    public Member create(MemberCreateDto dto) {
        return TxManager.of(txm).tx(() -> Member.create(dto, rep)).orElse(null);
    }
    
    public Member edit(String memberId, MemberCreateDto dto) {
        return TxManager.of(txm).tx(() -> Member.edit(memberId, dto, rep)).orElse(null);
    }
    
    public Member delete(String memberId) {
        return TxManager.of(txm).tx(() -> Member.delete(memberId, rep)).orElse(null);
    }

}
