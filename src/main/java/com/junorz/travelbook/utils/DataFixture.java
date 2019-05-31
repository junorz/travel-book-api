package com.junorz.travelbook.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.ImmutableList;
import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.AccessUrl;
import com.junorz.travelbook.domain.Detail;
import com.junorz.travelbook.domain.Member;
import com.junorz.travelbook.domain.PrimaryCategory;
import com.junorz.travelbook.domain.SecondaryCategory;
import com.junorz.travelbook.domain.TravelBook;

/**
 * Initialize test data.<p>
 * Set travelbook.datafixture in application.yml to false if you are in the production environment.
 */
@Component
public class DataFixture {
    
    @Value("${travelbook.datafixture}")
    private boolean isDataFixture;
    
    private final Repository rep;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager txm;
    
    
    public DataFixture(Repository rep, PasswordEncoder passwordEncoder, PlatformTransactionManager txm) {
        this.rep = rep;
        this.passwordEncoder = passwordEncoder;
        this.txm = txm;
    }
    
    @PostConstruct
    public void init() {
        if (isDataFixture) {
            TxManager.of(txm).tx(() -> {
                PrimaryCategory p1 = new PrimaryCategory();
                p1.setName("P1");
                PrimaryCategory p2 = new PrimaryCategory();
                p2.setName("P2");
                
                SecondaryCategory s1 = new SecondaryCategory();
                s1.setName("S1");
                s1.setPrimaryCategory(p1);

                SecondaryCategory s2 = new SecondaryCategory();
                s2.setName("S2");
                s2.setPrimaryCategory(p1);

                SecondaryCategory s3 = new SecondaryCategory();
                s3.setName("S3");
                s3.setPrimaryCategory(p1);

                SecondaryCategory s4 = new SecondaryCategory();
                s4.setName("S4");
                s4.setPrimaryCategory(p2);

                SecondaryCategory s5 = new SecondaryCategory();
                s5.setName("S5");
                s5.setPrimaryCategory(p2);
                
                p1.setSecondaryCategoryList(ImmutableList.of(s1, s2, s3));
                p2.setSecondaryCategoryList(ImmutableList.of(s4, s5));
                
                rep.em().persist(p1);
                rep.em().persist(p2);
                
                TravelBook travelBook = new TravelBook();
                AccessUrl accessUrl = new AccessUrl();
                accessUrl.setUrl(AccessUrlUtil.getUrl());
                rep.em().persist(accessUrl);
                
                Member m1 = new Member();
                Member m2 = new Member();
                
                m1.setName("Member1");
                m1.setAvaliable(true);
                m1.setTravelBook(travelBook);
                m2.setName("Member2");
                m2.setAvaliable(true);
                m2.setTravelBook(travelBook);

                List<Member> memberList = ImmutableList.of(m1, m2);
                

                Detail d1 = new Detail();
                Detail d2 = new Detail();
                Detail d3 = new Detail();
                
                d1.setAmount(new BigDecimal("10000"));
                d1.setRemarks("remarks 1");
                d1.setAvaliable(true);
                d1.setCurrency(Currency.JPY);
                d1.setDateTime(LocalDateTime.now());
                d1.setExchangeRate("1");
                d1.setMember(m1);
                d1.setMemberList(ImmutableList.of(m1, m2));
                d1.setPrimaryCategory(p1);
                d1.setSecondaryCategory(s1);
                d1.setTravelBook(travelBook);
                
                d2.setAmount(new BigDecimal("5000"));
                d2.setRemarks("remarks 2");
                d2.setAvaliable(true);
                d2.setCurrency(Currency.JPY);
                d2.setDateTime(LocalDateTime.now().minusDays(3));
                d2.setExchangeRate("1");
                d2.setMember(m2);
                d2.setMemberList(ImmutableList.of(m1, m2));
                d2.setPrimaryCategory(p1);
                d2.setSecondaryCategory(s2);
                d2.setTravelBook(travelBook);
                
                d3.setAmount(new BigDecimal("23000"));
                d3.setRemarks("remarks 3");
                d3.setAvaliable(true);
                d3.setCurrency(Currency.JPY);
                d3.setDateTime(LocalDateTime.now().minusDays(5));
                d3.setExchangeRate("1");
                d3.setMember(m2);
                d3.setMemberList(ImmutableList.of(m1));
                d3.setPrimaryCategory(p2);
                d3.setSecondaryCategory(s4);
                d3.setTravelBook(travelBook);
                List<Detail> detailList = ImmutableList.of(d1, d2, d3);
                
                travelBook.setName("Travelbook");
                travelBook.setAccessUrl(accessUrl);
                travelBook.setAdminPassword(passwordEncoder.encode("1234"));
                travelBook.setAvaliable(true);
                travelBook.setCreateDateTime(LocalDateTime.now());
                travelBook.setMemeberList(memberList);
                travelBook.setDetailList(detailList);
                travelBook.setCurrency(Currency.JPY);
                rep.em().persist(travelBook);
                
            });
        }
    }
}
