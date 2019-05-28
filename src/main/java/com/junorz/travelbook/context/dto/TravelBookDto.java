package com.junorz.travelbook.context.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.junorz.travelbook.domain.Detail;
import com.junorz.travelbook.domain.Member;
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
                Optional.ofNullable(travelBook.getMemeberList()).orElse(new ArrayList<Member>()).stream()
                        .filter(member -> member.isAvaliable())
                        .map(member -> MemberDto.of(member))
                        .collect(Collectors.toList()),
                Optional.ofNullable(travelBook.getDetailList()).orElse(new ArrayList<Detail>()).stream()
                        .filter(detail -> detail.isAvaliable())
                        .map(detail -> DetailDto.of(detail))
                        .collect(Collectors.toList()));
    }
}
