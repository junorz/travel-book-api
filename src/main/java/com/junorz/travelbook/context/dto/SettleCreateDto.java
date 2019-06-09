package com.junorz.travelbook.context.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SettleCreateDto {

    @NotEmpty(message = "{TRAVELBOOK_ID_CANNOT_BE_NULL}")
    private String travelBookId;

    @NotEmpty(message = "{MEMBER_ID_CANNOT_BE_NULL}")
    private String fromMemberId;

    @NotEmpty(message = "{MEMBER_ID_CANNOT_BE_NULL}")
    private String toMemberId;

    @NotEmpty(message = "{SETTLE_AMOUNT_CANNOT_BE_NULL}")
    private String amount;

}
