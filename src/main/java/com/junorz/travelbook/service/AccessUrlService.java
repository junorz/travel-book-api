package com.junorz.travelbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.AccessUrl;

@Service
public class AccessUrlService {
    
    private final Repository rep;
    private final PlatformTransactionManager txm;
    
    public AccessUrlService(Repository rep, PlatformTransactionManager txm) {
        this.rep = rep;
        this.txm = txm;
    }
    
    public AccessUrl findByUrl(String url) {
        return TxManager.of(txm).tx(() -> AccessUrl.findByUrl(url, rep));
    }
    
}
