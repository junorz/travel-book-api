package com.junorz.travelbook.context.dto;

import com.junorz.travelbook.domain.Settle;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SettleDto {

    private String id;
    private MemberDto fromMember;
    private MemberDto toMember;
    private BigDecimal amount;

    public static SettleDto of(Settle settle) {
        return new SettleDto(settle.getId(), MemberDto.of(settle.getFromMember()), MemberDto.of(settle.getToMember()), settle.getAmount());
    }

}
