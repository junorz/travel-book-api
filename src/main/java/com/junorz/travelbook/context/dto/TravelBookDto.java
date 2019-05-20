package com.junorz.travelbook.context.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.junorz.travelbook.domain.TravelBook;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The TravelBook DTO for presentation layer.<br>
 */
@Data
@AllArgsConstructor
public class TravelBookDto {
    private String id;
    private String name;
    private String accessUrl;
    private String currency;
    private List<MemberDto> memberList;
    private List<DetailDto> detailList;

    public static TravelBookDto of(TravelBook travelBook) {
        return new TravelBookDto(travelBook.getId(), travelBook.getName(), travelBook.getAccessUrl().getUrl(),
                travelBook.getCurrency().toString(),
                travelBook.getMemeberList().stream().map(member -> MemberDto.of(member)).collect(Collectors.toList()),
                travelBook.getDetailList().stream().map(detail -> DetailDto.of(detail)).collect(Collectors.toList()));
    }
}
