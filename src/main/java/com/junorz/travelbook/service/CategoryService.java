package com.junorz.travelbook.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.PrimaryCategory;

@Service
public class CategoryService {
    
    private final Repository rep;
    private final PlatformTransactionManager txm;
    
    public CategoryService(Repository rep, PlatformTransactionManager txm) {
        this.rep = rep;
        this.txm = txm;
    }
    
    public List<PrimaryCategory> findAll() {
        return TxManager.of(txm).tx(() -> PrimaryCategory.findAll(rep));
    }
    
}
