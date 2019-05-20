package com.junorz.travelbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.Detail;

@Service
public class DetailService {
    
    private final Repository rep;
    private final PlatformTransactionManager txm;
    
    public DetailService(Repository rep, PlatformTransactionManager txm) {
        this.rep = rep;
        this.txm = txm;
    }
    
    public Detail create(DetailCreateDto dto) {
        return TxManager.of(txm).tx(() -> Detail.create(dto, rep));
    }
    
}
