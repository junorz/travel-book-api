package com.junorz.travelbook.context.dto;

import java.time.LocalDateTime;
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
    private String primaryCategoryName;
    private String secondaryCategoryName;
    private String amount;
    private String member;
    private List<String> memberList;
    private String currency;
    private String exchangeRate;
    private LocalDateTime dateTime;
    private String remarks;

    public static DetailDto of(Detail detail) {
        return new DetailDto(detail.getId(), detail.getTravelBook().getId(), detail.getPrimaryCategory().getName(),
                detail.getSecondaryCategory().getName(), detail.getAmount().toString(), detail.getMember().getName(),
                detail.getMemberList().stream().map(m -> m.getName()).collect(Collectors.toList()),
                detail.getCurrency().toString(), detail.getExchangeRate(), detail.getDateTime(),
                detail.getRemarks());
    }
}
