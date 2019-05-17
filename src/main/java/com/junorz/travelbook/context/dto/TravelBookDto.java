package com.junorz.travelbook.context.dto;

import java.util.List;

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
    private List<Member> memberList;
    private List<Detail> detailList;

    public static TravelBookDto of(TravelBook travelBook) {
        return new TravelBookDto(travelBook.getId(), travelBook.getName(), travelBook.getAccessUrl().getUrl(),
                travelBook.getCurrency().toString(), travelBook.getMemeberList(), travelBook.getDetailList());
    }
}
