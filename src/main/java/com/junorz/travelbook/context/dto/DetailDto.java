package com.junorz.travelbook.context.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.junorz.travelbook.domain.Detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailDto {
    private String id;
    private String travelBookId;
    private long primaryCategoryId;
    private String primaryCategoryName;
    private long secondaryCategoryId;
    private String secondaryCategoryName;
    private String amount;
    private MemberDto member;
    private List<MemberDto> memberList;
    private String currency;
    private String exchangeRate;
    private ZonedDateTime dateTime;
    private String remarks;

    public static DetailDto of(Detail detail) {
        return new DetailDto(
                detail.getId(),
                detail.getTravelBook().getId(),
                detail.getPrimaryCategory().getId(),
                detail.getPrimaryCategory().getName(),
                detail.getSecondaryCategory().getId(),
                detail.getSecondaryCategory().getName(),
                detail.getAmount().toString(),
                MemberDto.of(detail.getMember()),
                detail.getMemberList().stream().map(m -> MemberDto.of(m)).collect(Collectors.toList()),
                detail.getCurrency().toString(),
                detail.getExchangeRate(),
                detail.getDateTime(),
                detail.getRemarks());
    }

}
