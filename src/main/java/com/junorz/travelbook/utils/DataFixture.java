package com.junorz.travelbook.utils;

import com.google.common.collect.ImmutableList;
import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                p1.setName("餐饮费用");
                SecondaryCategory p1s1 = SecondaryCategory.of("零食饮料", p1);
                SecondaryCategory p1s2 = SecondaryCategory.of("聚餐吃饭", p1);
                SecondaryCategory p1s3 = SecondaryCategory.of("风味小吃", p1);
                p1.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p1s1, p1s2, p1s3)));

                PrimaryCategory p2 = new PrimaryCategory();
                p2.setName("交通费用");
                SecondaryCategory p2s1 = SecondaryCategory.of("飞机票", p2);
                SecondaryCategory p2s2 = SecondaryCategory.of("火车票", p2);
                SecondaryCategory p2s3 = SecondaryCategory.of("汽车票", p2);
                SecondaryCategory p2s4 = SecondaryCategory.of("轮船票", p2);
                SecondaryCategory p2s5 = SecondaryCategory.of("的士费", p2);
                SecondaryCategory p2s6 = SecondaryCategory.of("租车费", p2);
                SecondaryCategory p2s7 = SecondaryCategory.of("加油费", p2);
                SecondaryCategory p2s8 = SecondaryCategory.of("高速费", p2);
                SecondaryCategory p2s9 = SecondaryCategory.of("停车费", p2);
                p2.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p2s1, p2s2, p2s3, p2s4, p2s5, p2s6, p2s7, p2s8, p2s9)));

                PrimaryCategory p3 = new PrimaryCategory();
                p3.setName("娱乐费用");
                SecondaryCategory p3s1 = SecondaryCategory.of("景点门票", p3);
                SecondaryCategory p3s2 = SecondaryCategory.of("购物消费", p3);
                SecondaryCategory p3s3 = SecondaryCategory.of("KTV消费", p3);
                SecondaryCategory p3s4 = SecondaryCategory.of("旅游团费", p3);
                SecondaryCategory p3s5 = SecondaryCategory.of("向导费用", p3);
                p3.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p3s1, p3s2, p3s3, p3s4, p3s5)));

                PrimaryCategory p4 = new PrimaryCategory();
                p4.setName("住宿费用");
                SecondaryCategory p4s1 = SecondaryCategory.of("住宿房费", p4);
                SecondaryCategory p4s2 = SecondaryCategory.of("住宿小费", p4);
                SecondaryCategory p4s3 = SecondaryCategory.of("停车费用", p4);
                p4.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p4s1, p4s2, p4s3)));

                PrimaryCategory p5 = new PrimaryCategory();
                p5.setName("旅游用品");
                SecondaryCategory p5s1 = SecondaryCategory.of("户外衣裤", p5);
                SecondaryCategory p5s2 = SecondaryCategory.of("户外衣裤", p5);
                SecondaryCategory p5s3 = SecondaryCategory.of("数码设备", p5);
                SecondaryCategory p5s4 = SecondaryCategory.of("个护用品", p5);
                SecondaryCategory p5s5 = SecondaryCategory.of("其他用品", p5);
                p5.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p5s1, p5s2, p5s3, p5s4, p5s5)));

                PrimaryCategory p6 = new PrimaryCategory();
                p6.setName("其他消费");
                SecondaryCategory p6s1 = SecondaryCategory.of("流量费用", p6);
                SecondaryCategory p6s2 = SecondaryCategory.of("其他杂费", p6);
                p6.setSecondaryCategoryList(new ArrayList<>(Arrays.asList(p6s1, p6s2)));

                rep.em().persist(p1);
                rep.em().persist(p2);
                rep.em().persist(p3);
                rep.em().persist(p4);
                rep.em().persist(p6);
                rep.em().persist(p6);

                TravelBook travelBook = new TravelBook();
                AccessUrl accessUrl = new AccessUrl();
                accessUrl.setUrl(AccessUrlUtil.getUrl());
                rep.em().persist(accessUrl);
                
                Member m1 = new Member();
                Member m2 = new Member();
                
                m1.setName("Member1");
                m1.setTravelBook(travelBook);
                m2.setName("Member2");
                m2.setTravelBook(travelBook);

                List<Member> memberList = ImmutableList.of(m1, m2);
                

                Detail d1 = new Detail();
                Detail d2 = new Detail();
                Detail d3 = new Detail();
                
                d1.setAmount(new BigDecimal("10000"));
                d1.setRemarks("remarks 1");
                d1.setAvaliable(true);
                d1.setCurrency(Currency.JPY);
                d1.setDateTime(ZonedDateTime.now());
                d1.setExchangeRate("1");
                d1.setMember(m1);
                d1.setMemberList(ImmutableList.of(m1, m2));
                d1.setPrimaryCategory(p1);
                d1.setSecondaryCategory(p1s1);
                d1.setTravelBook(travelBook);
                
                d2.setAmount(new BigDecimal("5000"));
                d2.setRemarks("remarks 2");
                d2.setAvaliable(true);
                d2.setCurrency(Currency.JPY);
                d2.setDateTime(ZonedDateTime.now().minusDays(3));
                d2.setExchangeRate("1");
                d2.setMember(m2);
                d2.setMemberList(ImmutableList.of(m1, m2));
                d2.setPrimaryCategory(p1);
                d2.setSecondaryCategory(p1s2);
                d2.setTravelBook(travelBook);
                
                d3.setAmount(new BigDecimal("23000"));
                d3.setRemarks("remarks 3");
                d3.setAvaliable(true);
                d3.setCurrency(Currency.JPY);
                d3.setDateTime(ZonedDateTime.now().minusDays(5));
                d3.setExchangeRate("1");
                d3.setMember(m2);
                d3.setMemberList(ImmutableList.of(m1));
                d3.setPrimaryCategory(p2);
                d3.setSecondaryCategory(p2s1);
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
