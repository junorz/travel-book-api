package com.junorz.travelbook.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

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
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.exception.ResourceNotFoundException;
import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class Detail {

    @Id
    @GeneratedValue(generator = "detailIdGen")
    @GenericGenerator(name = "detailIdGen", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "memeber_id", referencedColumnName = "id")
    private Member member;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category_id", referencedColumnName = "id")
    private PrimaryCategory primaryCategory;

    @NotNull
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
    
    private boolean isAvaliable = true;

    public static Optional<Detail> findById(String id, Repository rep) {
        return Optional.ofNullable(rep.em().find(Detail.class, id));
    }

    public static Detail create(DetailCreateDto dto, Repository rep) {
        TravelBook travelBook = TravelBook.findById(dto.getTravelBookId(), rep)
                .orElseThrow(supplyRnf(Messages.NO_TRAVELBOOK_FOUND));
        Member member = Member.findById(dto.getMemberId(), rep).orElseThrow(supplyRnf(Messages.MEMBER_NOT_FOUND));
        PrimaryCategory primaryCategory = PrimaryCategory.findById(Long.parseLong(dto.getPrimaryCategoryId()), rep)
                .orElseThrow(supplyRnf(Messages.PRIMARY_CATEGORY_NOT_FOUND));
        SecondaryCategory secondaryCategory = SecondaryCategory
                .findById(Long.parseLong(dto.getSecondaryCategoryId()), rep)
                .orElseThrow(supplyRnf(Messages.SECONDARY_CATEGORY_NOT_FOUND));

        Detail detail = new Detail();
        detail.setTravelBook(travelBook);
        detail.setMember(member);
        detail.setPrimaryCategory(primaryCategory);
        detail.setSecondaryCategory(secondaryCategory);
        detail.setAmount(new BigDecimal(dto.getAmount()));
        detail.setCurrency(Currency.valueOf(dto.getCurrency()));
        detail.setExchangeRate(dto.getExchangeRate());
        detail.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        detail.setRemarks(dto.getRemarks());

        rep.em().persist(detail);
        return detail;
    }

    public static Detail edit(String id, DetailCreateDto dto, Repository rep) {
        Detail detail = Detail.findById(id, rep).orElseThrow(supplyRnf(Messages.DETAIL_NOT_FOUND));
        Member member = Member.findById(dto.getMemberId(), rep).orElseThrow(supplyRnf(Messages.MEMBER_NOT_FOUND));
        PrimaryCategory primaryCategory = PrimaryCategory.findById(Long.parseLong(dto.getPrimaryCategoryId()), rep).orElseThrow(supplyRnf(Messages.PRIMARY_CATEGORY_NOT_FOUND));
        SecondaryCategory secondaryCategory = SecondaryCategory.findById(Long.parseLong(dto.getSecondaryCategoryId()), rep).orElseThrow(supplyRnf(Messages.SECONDARY_CATEGORY_NOT_FOUND));
        
        detail.setMember(member);
        detail.setPrimaryCategory(primaryCategory);
        detail.setSecondaryCategory(secondaryCategory);
        detail.setAmount(new BigDecimal(dto.getAmount()));
        detail.setCurrency(Currency.valueOf(dto.getCurrency()));
        detail.setExchangeRate(dto.getExchangeRate());
        detail.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        detail.setRemarks(dto.getRemarks());
        
        rep.em().merge(detail);
        return detail;
    }
    
    public static Detail delete(String id, Repository rep) {
        Detail detail = Detail.findById(id, rep).orElseThrow(supplyRnf(Messages.DETAIL_NOT_FOUND));
        detail.setAvaliable(false);
        return detail;
    }

    private static Supplier<ResourceNotFoundException> supplyRnf(String message) {
        return new Supplier<ResourceNotFoundException>() {
            @Override
            public ResourceNotFoundException get() {
                return new ResourceNotFoundException(message);
            }
        };
    }

}
