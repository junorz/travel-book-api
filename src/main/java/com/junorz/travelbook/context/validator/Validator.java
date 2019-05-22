package com.junorz.travelbook.context.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import com.junorz.travelbook.context.consts.Messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Validator {

    private MessageSource messageSource;

    public static <T> boolean validate(ValidateType validateType, T object) {
        return validate(validateType, object, "");
    }
    
    public static <T> boolean validate(ValidateType validateType, T object, String errorMessageCode) {
        switch (validateType) {
        case REQUIRED:
            if (object == null) {
                throw new ValidationException(errorMessageCode);
            }
            return true;
        default:
            return false;
        }
    }
    
    public static <T> boolean validate(ValidateType validateType, List<T> list, String errorMessageCode) {
        switch (validateType) {
        case REQUIRED:
            if (list.size() == 0) {
                throw new ValidationException(errorMessageCode);
            }
            return true;
        default:
            return false;
        }
    }
    
    public static <T> boolean validate(ValidateType validateType, T obj1, T obj2, String errorMessageCode) {
        switch (validateType) {
        case EQUALS:
            if (obj1 != obj2) {
                // try equals method
                if (!obj1.equals(obj2)) {
                    throw new ValidationException(errorMessageCode);
                }
            }
            return true;
        default:
            return false;
        }
    }
    
    /**
     * Due to the specification of TokenValidatonFilter, the travelbook's Id must be contained in request body,<br>
     * and it can not be null.<br>
     * If travelbook's ID appears in URL, you must verify if the one in URL and the one in request body are the same for security issue.
     */
    public static boolean validateTbId(String idInUrl, String idInReqeustBody) {
        if (StringUtils.isBlank(idInReqeustBody) || !idInUrl.equals(idInReqeustBody)) {
            throw new ValidationException(Messages.BAD_REQUEST);
        }
        return true;
    }
}
