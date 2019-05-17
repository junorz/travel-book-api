package com.junorz.travelbook.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

import lombok.Data;

@Entity
@Data
public class Detail {
    
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "memeber_id", referencedColumnName = "id")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;
    
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
    
}
