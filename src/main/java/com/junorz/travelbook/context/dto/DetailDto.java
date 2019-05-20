package com.junorz.travelbook.context.dto;

import com.junorz.travelbook.domain.Detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailDto {
    private String id;
    private String travelBookId;
    private String primaryCategoryName;
    private String secondaryCategoryName;
    private String amount;
    private String currency;
    private String exchangeRate;
    private String dateTime;
    private String remarks;

    public static DetailDto of(Detail detail) {
        return new DetailDto(detail.getId(), detail.getTravelBook().getId(), detail.getPrimaryCategory().getName(),
                detail.getSecondaryCategory().getName(), detail.getAmount().toString(), detail.getCurrency().toString(),
                detail.getExchangeRate(), detail.getDateTime().toString(), detail.getRemarks());
    }
}
