package com.junorz.travelbook.context.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class DetailCreateDto {
    @NotBlank(message = "{TRAVELBOOK_ID_CANNOT_BE_NULL}")
    private String travelBookId;
    
    @NotBlank(message = "{MEMBER_ID_CANNOT_BE_NULL}")
    private String memberId;
    
    @NotEmpty(message = "{MEMBER_ID_CANNOT_BE_NULL}")
    private List<String> memberList;
    
    @NotBlank(message = "{PRIMARY_CATEGORY_ID_CANNOT_BE_NULL}")
    private String primaryCategoryId;
    
    @NotBlank(message = "{SECONDARY_CATEGORY_ID_CANNOT_BE_NULL}")
    private String secondaryCategoryId;
    
    @NotBlank(message = "{DETAIL_AMOUNT_CANNOT_BE_NULL}")
    private String amount;
    
    @NotBlank(message = "{DETAIL_CURRENCY_CANNOT_BE_NULL}")
    private String currency;
    
    @NotBlank(message = "{DETAIL_EXCHANGE_RATE_CANNOT_BE_NULL}")
    private String exchangeRate;
    
    @NotBlank(message = "{DETAIL_DATETIME_CANNOT_BE_NULL}")
    private String dateTime;
    
    @NotBlank(message = "{DETAIL_REMARKS_CANNOT_BE_NULL}")
    private String remarks;
    
}
