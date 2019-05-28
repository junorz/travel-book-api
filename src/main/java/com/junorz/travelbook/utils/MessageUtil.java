package com.junorz.travelbook.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtil {
    
    private static MessageSource MESSAGE_SOURCE;
    
    public MessageUtil(MessageSource messageSource) {
        MessageUtil.MESSAGE_SOURCE = messageSource;
    }
    
    public static String getMessage(String msgCode) {
        return MESSAGE_SOURCE.getMessage(msgCode, null, LocaleContextHolder.getLocale());
    }
    
}
