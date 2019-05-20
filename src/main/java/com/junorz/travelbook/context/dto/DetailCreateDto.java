package com.junorz.travelbook.context.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DetailCreateDto {
    @NotNull(message = "{TRAVELBOOK_ID_CANNOT_BE_NULL}")
    private String travelBookId;
    
    @NotNull(message = "{MEMBER_ID_CANNOT_BE_NULL}")
    private String memberId;
    
    @NotNull(message = "{PRIMARY_CATEGORY_ID_CANNOT_BE_NULL}")
    private String primaryCategoryId;
    
    @NotNull(message = "{SECONDARY_CATEGORY_ID_CANNOT_BE_NULL}")
    private String secondaryCategoryId;
    
    @NotNull(message = "{DETAIL_AMOUNT_CANNOT_BE_NULL}")
    private String amount;
    
    @NotNull(message = "{DETAIL_CURRENCY_CANNOT_BE_NULL}")
    private String currency;
    
    @NotNull(message = "{DETAIL_EXCHANGE_RATE_CANNOT_BE_NULL}")
    private String exchangeRate;
    
    @NotNull(message = "{DETAIL_DATETIME_CANNOT_BE_NULL}")
    private String dateTime;
    
    private String remarks;
    
}
