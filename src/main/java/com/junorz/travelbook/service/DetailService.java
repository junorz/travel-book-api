package com.junorz.travelbook.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.audit.AuditHandler;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.Detail;
import com.junorz.travelbook.utils.MessageUtil;

@Service
public class DetailService {

    private static final Logger logger = LoggerFactory.getLogger(DetailService.class);

    private final Repository rep;
    private final PlatformTransactionManager txm;
    private final AuditHandler audit;

    public DetailService(Repository rep, PlatformTransactionManager txm, AuditHandler audit) {
        this.rep = rep;
        this.txm = txm;
        this.audit = audit;
    }

    public Detail create(DetailCreateDto dto) {
        return audit.audit(() -> {
            Detail detail = TxManager.of(txm).tx(() -> Detail.create(dto, rep));
            logger.info(MessageUtil.getMessage(Messages.LOG_DETAIL_CREATE_SUCCESS), detail.getId(),
                    detail.getTravelBook().getId(), detail.getAmount(), detail.getRemarks(),
                    detail.getMember().getName(),
                    detail.getMemberList().stream().map(m -> m.getName()).collect(Collectors.toList()).toString(),
                    detail.getPrimaryCategory().getName(), detail.getSecondaryCategory().getName(),
                    detail.getCurrency().toString(), detail.getExchangeRate(), detail.getDateTime());
            return detail;
        });
    }

    public Detail edit(String id, DetailCreateDto dto) {
        return TxManager.of(txm).tx(() -> Detail.edit(id, dto, rep));
    }

    public Detail delete(String id) {
        return TxManager.of(txm).tx(() -> Detail.delete(id, rep));
    }

}
