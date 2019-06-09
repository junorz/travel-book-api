package com.junorz.travelbook.service;

import com.junorz.travelbook.context.audit.AuditHandler;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.SettleCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.Settle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Service
public class SettleService {

    private static final Logger logger = LoggerFactory.getLogger(SettleService.class);

    private final Repository rep;
    private final PlatformTransactionManager txm;
    private final AuditHandler audit;

    @Autowired
    public SettleService(Repository rep, PlatformTransactionManager txm, AuditHandler audit) {
        this.rep = rep;
        this.txm = txm;
        this.audit = audit;
    }

    public List<Settle> findAll(String travelBookId) {
        return TxManager.of(txm).tx(() -> Settle.findAll(travelBookId, rep));
    }


    public Settle create(SettleCreateDto dto) {
        return audit.audit(() -> {
            Settle settle = TxManager.of(txm).tx(() -> Settle.create(dto, rep));
            logger.info(Messages.LOG_SETTLE_CREATE_SUCCESS, settle.getId(), settle.getTravelBook(), settle.getFromMember().getName(), settle.getToMember().getName(), settle.getAmount());
            return settle;
        });

    }


}
