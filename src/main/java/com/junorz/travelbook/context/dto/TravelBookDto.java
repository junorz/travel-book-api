package com.junorz.travelbook.context.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
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

    // call this method if you want to calculate settlement before returning
    // to the presentation layer.
    // P.S. no need to call this method if you want to calculate in presentation layer.(not recommended)
    public TravelBookDto calculate() {
        // calculate total amount
        BigDecimal totalAmount = detailList.stream()
                .map(d -> new BigDecimal(d.getAmount()).multiply(new BigDecimal(d.getExchangeRate())))
                .reduce((result, next) -> result.add(next)).orElse(BigDecimal.ZERO);
        calculation.setAmount(totalAmount);

        // initialize settle details with member name, and set other properties to 0.
        memberList.forEach(m -> calculation.settleDetails
                .add(new SettleDetail(m.getName(), BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<Path>())));
        
        // initialize path list
        calculation.settleDetails.forEach(s -> {
            memberList.forEach(m -> {
                if (!m.getName().equals(s.getName())) {
                    s.getPaths().add(new Path(m.getName()));
                }
            });
        });
        
        detailList.forEach(d -> {
            // calculate paid amount.
            for (SettleDetail s : calculation.settleDetails) {
                if (d.getMember().getName().equals(s.getName())) {
                    s.setPaid(s.getPaid().add(new BigDecimal(d.getAmount())
                            .multiply(new BigDecimal(d.getExchangeRate()))));
                    break;
                }
            }

            // calculate average amount.
            int memberCount = d.getMemberList().size();
            BigDecimal average = new BigDecimal(d.getAmount()).multiply(new BigDecimal(d.getExchangeRate()))
                    .divide(new BigDecimal(memberCount), 10, RoundingMode.HALF_UP);
            d.getMemberList().forEach(m ->
                calculation.settleDetails.forEach(s -> {
                    if (m.getName().equals(s.getName())) {
                        s.setAverage(s.getAverage().add(average));
                        
                        // set settle path
                        // Conditions:
                        // 1. If the member does not the payer.
                        // 2. The name in settle details matches.
                        if (!m.getName().equals(d.getMember().getName())) {
                            s.getPaths().forEach(p -> {
                                if (p.getToName().equals(d.getMember().getName())) {
                                    p.setAmount(p.getAmount().add(average));
                                }
                            });
                        }
                    }
                })
            );
            
        });
        
        // Drop path with 0 amount.
        calculation.settleDetails.forEach(s -> {
            Iterator<Path> it = s.getPaths().iterator();
            while(it.hasNext()) {
                Path p = it.next();
                if (p.getAmount().equals(BigDecimal.ZERO)) {
                    it.remove();
                }
            }
        });
        
        // Make the settle path more intelligent (BETA)
        calculation.settleDetails.forEach(s -> {
            Iterator<Path> itPath1 = s.getPaths().iterator();
            while (itPath1.hasNext()) {
                Path p1 = itPath1.next();
                calculation.settleDetails.forEach(s2 -> {
                    if(p1.getToName().equals(s2.getName())) {
                        Iterator<Path> itPath2 = s2.getPaths().iterator();
                        while (itPath2.hasNext()) {
                            Path p2 = itPath2.next();
                            if(p2.getToName().equals(s.getName())) {
                                if (0 < p1.getAmount().compareTo(p2.getAmount())) {
                                    p1.setAmount(p1.getAmount().subtract(p2.getAmount()));
                                    itPath2.remove();
                                } else {
                                    p2.setAmount(p2.getAmount().subtract(p1.getAmount()));
                                    itPath1.remove();
                                }
                            }
                        }
                    }
                });
            } 
        });
        return this;
    }

    @Data
    private static class Calculation {
        // Total amount
        private BigDecimal amount;

        // details of payers
        // a list of { "name": xxx, "paid": xxx, "average": xxx, "balance": xxx, "path": xxx }
        private List<SettleDetail> settleDetails = new ArrayList<>();

    }

    @Data
    @AllArgsConstructor
    private static class SettleDetail {
        private String name;
        private BigDecimal paid;
        private BigDecimal average;
        private List<Path> paths;
    }
    
    @Data
    private static class Path {
        
        public Path(String toName) {
            this.toName = toName;
        }
        
        // pay to whom
        private String toName;
        // amount need to pay
        private BigDecimal amount = BigDecimal.ZERO;
    }

}
