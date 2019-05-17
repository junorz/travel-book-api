package com.junorz.travelbook.context.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MemberCreateDto {
    @NotNull(message = "{TRAVELBOOK_ID_CANNOT_BE_NULL}")
    private String travelBookId;
    @NotNull(message = "{MEMBER_NAME_CANNOT_BE_NULL}")
    private String memberName;
}
