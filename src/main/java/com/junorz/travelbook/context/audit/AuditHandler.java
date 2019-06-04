package com.junorz.travelbook.context.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.exception.InvalidOperationException;
import com.junorz.travelbook.context.exception.ResourceNotFoundException;
import com.junorz.travelbook.context.exception.ValidationException;

public class AuditHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditHandler.class);
    private final MessageSource messageSource;
    
    @Autowired
    public AuditHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public void audit(String msgCode, Object[] args, Runnable runnable) {
        if (!Strings.isNullOrEmpty(msgCode)) {
            String message = messageSource.getMessage(msgCode, args, LocaleContextHolder.getLocale());
            // log
            logger.info(message);
        }
        try {
            runnable.run();
        } catch (ResourceNotFoundException | ValidationException | InvalidOperationException e) {
            logger.error(messageSource.getMessage(Messages.RESOURCE_NOT_FOUND, null, LocaleContextHolder.getLocale()));
            throw e;
        }
    }
    
    public void audit(String msgCode, Runnable runnable) {
        audit(msgCode, null, runnable);
    }
    
    public void audit(Runnable runnable) {
        audit(null, runnable);
    }
    
    public <T> T audit(String msgCode, Object[] args, Supplier<T> supplier) {
        if (!Strings.isNullOrEmpty(msgCode)) {
            String message = messageSource.getMessage(msgCode, args, LocaleContextHolder.getLocale());
            // log
            logger.info(message);
        }
        T result = null;
        try {
            result = supplier.get();
        } catch (ResourceNotFoundException | ValidationException | InvalidOperationException e) {
            logger.error(messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale()));
            throw e;
        } 
        return result;
    }
    
    public <T> T audit(String msgCode, Supplier<T> supplier) {
        return audit(msgCode, null, supplier);
    }
    
    public <T> T audit(Supplier<T> supplier) {
        return audit(null, supplier);
    }
    
}
