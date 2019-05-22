package com.junorz.travelbook.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class Detail {

    @Id
    @GeneratedValue(generator = "detailIdGen")
    @GenericGenerator(name = "detailIdGen", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;

    @ManyToOne
    @JoinColumn(name = "memeber_id", referencedColumnName = "id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category_id", referencedColumnName = "id")
    private PrimaryCategory primaryCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_categroy_id", referencedColumnName = "id")
    private SecondaryCategory secondaryCategory;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    @Column(name = "exchange_rate")
    private String exchangeRate;

    @NotNull
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String remarks;

    public static Detail create(DetailCreateDto dto, Repository rep) {
        List<TravelBook> travelbookList = rep.em()
                .createQuery("SELECT tb FROM TravelBook tb WHERE tb.id = ?1", TravelBook.class)
                .setParameter(1, dto.getTravelBookId()).getResultList();
        List<PrimaryCategory> primaryCategoriList = rep.em()
                .createQuery("SELECT pc FROM PrimaryCategory pc WHERE pc.id = ?1", PrimaryCategory.class)
                .setParameter(1, Long.parseLong(dto.getPrimaryCategoryId())).getResultList();
        List<SecondaryCategory> secondaryCategoryList = rep.em()
                .createQuery("SELECT sc FROM SecondaryCategory sc WHERE sc.id = ?1", SecondaryCategory.class)
                .setParameter(1, Long.parseLong(dto.getSecondaryCategoryId())).getResultList();
        if (travelbookList.size() == 0 || primaryCategoriList.size() == 0 || secondaryCategoryList.size() == 0) {
            throw new RuntimeException("Categories could not be found.");
        }
        TravelBook tb = travelbookList.get(0);
        PrimaryCategory pc = primaryCategoriList.get(0);
        SecondaryCategory sc = secondaryCategoryList.get(0);

        Detail detail = new Detail();
        detail.setTravelBook(tb);
        detail.setPrimaryCategory(pc);
        detail.setSecondaryCategory(sc);
        detail.setAmount(new BigDecimal(dto.getAmount()));
        detail.setCurrency(Currency.valueOf(dto.getCurrency()));
        detail.setExchangeRate(dto.getExchangeRate());
        detail.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        detail.setRemarks(dto.getRemarks());

        rep.em().persist(detail);
        return detail;
    }

}
