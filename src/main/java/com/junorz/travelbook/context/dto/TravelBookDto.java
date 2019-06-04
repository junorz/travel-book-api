package com.junorz.travelbook.context.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private Calculation calculation;

    // you can return a pure dto without any calculation.
    public static TravelBookDto of(TravelBook travelBook) {
        return new TravelBookDto(travelBook.getId(), travelBook.getName(), travelBook.getAccessUrl().getUrl(),
                travelBook.getCurrency().toString(),
                Optional.ofNullable(travelBook.getMemeberList()).orElse(new ArrayList<Member>()).stream()
                        .filter(member -> member.isAvaliable())
                        .map(member -> MemberDto.of(member))
                        .collect(Collectors.toList()),
                Optional.ofNullable(travelBook.getDetailList()).orElse(new ArrayList<Detail>()).stream()
                        .filter(detail -> detail.isAvaliable()).map(detail -> DetailDto.of(detail))
                        .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())).collect(Collectors.toList()),
                new Calculation());
    }

    // call this method if you want to calculate settlement result before returning
    // to the presentation layer.
    // no need to call this method if you want to calculate in presentation layer.
    public TravelBookDto calculate() {
        // calculate total amount
        BigDecimal totalAmount = detailList.stream()
                .map(d -> new BigDecimal(d.getAmount()).multiply(new BigDecimal(d.getExchangeRate())))
                .reduce((result, next) -> result.add(next)).get();
        calculation.setAmount(totalAmount);

        // initialize settle details with member name, and set other properties to 0.
        memberList.forEach(m -> calculation.settleDetails
                .add(new SettleDetail(m.getName(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)));

        detailList.forEach(d -> {
            // calculate paid amount.
            for (SettleDetail s : calculation.settleDetails) {
                if (d.getMember().equals(s.getName())) {
                    s.setPaid(s.getPaid().add(new BigDecimal(d.getAmount())));
                    break;
                }
            }

            // calculate average amount.
            int memberCount = d.getMemberList().size();
            BigDecimal average = new BigDecimal(d.getAmount()).divide(new BigDecimal(memberCount), 10,
                    RoundingMode.HALF_UP);
            d.getMemberList().forEach(m -> {
                for (SettleDetail s : calculation.settleDetails) {
                    if (m.equals(s.getName())) {
                        s.setAverage(s.getAverage().add(average));
                        break;
                    }
                }
            });
        });
        
        // calculate balance
        calculation.settleDetails.forEach(s -> {
            s.setBalance(s.getPaid().subtract(s.getAverage()));
        });

        return this;
    }

    @Data
    private static class Calculation {
        // Total amount
        private BigDecimal amount;

        // details of payers
        // a list of { "name": xxx, "paid": xxx, "average": xxx }
        private List<SettleDetail> settleDetails = new ArrayList<>();

    }

    @Data
    @AllArgsConstructor
    private static class SettleDetail {
        private String name;
        private BigDecimal paid;
        private BigDecimal average;
        private BigDecimal balance;
    }

}
