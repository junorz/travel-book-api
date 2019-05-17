package com.junorz.travelbook.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.context.dto.TravelBookCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.utils.AccessUrlUtil;

import lombok.Data;

@Entity
@Data
public class TravelBook {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;

    @NotNull
    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "travelBook")
    @JsonManagedReference
    private AccessUrl accessUrl;

    @NotNull
    private String adminPassword;

    @NotNull
    private Currency currency;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "travelBook")
    private List<Member> memeberList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "travelBook")
    private List<Detail> detailList;

    @NotNull
    private boolean isAvaliable = true;

    @NotNull
    private LocalDateTime createDateTime;

    public static List<TravelBook> findAll(Repository rep) {
        return rep.em().createQuery("SELECT tb FROM TravelBook tb", TravelBook.class).getResultList();
    }
    
    public static TravelBook findById(String id, Repository rep) {
        List<TravelBook> travelBookList = rep.em().createQuery("SELECT tb FROM TravelBook tb WHERE tb.id = ?1", TravelBook.class)
                .setParameter(1, id)
                .getResultList();
        return travelBookList.size() > 0 ? travelBookList.get(0) : null; 
    }

    public static TravelBook create(TravelBookCreateDto dto, Repository rep) {
        // trying to create an unique access url.
        String accessUrl = AccessUrlUtil.getUrl();
        boolean isAccessUrlAvaliable = AccessUrlUtil.checkUrlUnique(accessUrl, rep);
        if(!isAccessUrlAvaliable) {
            int retry = 0;
            // try 10 times
            for(int i = 0; i < 10; i++) {
                accessUrl = AccessUrlUtil.getUrl();
                if(AccessUrlUtil.checkUrlUnique(accessUrl, rep)) break;
                retry++;
            }
            if (retry == 10) {
                // throw an exception and roll back.
                throw new RuntimeException("Access url creation failed: too many attempts.");
            }
        }
        TravelBook travelBook = new TravelBook();
        AccessUrl au = new AccessUrl();
        au.setTravelBook(travelBook);
        au.setUrl(accessUrl);
        
        travelBook.setName(dto.getName());
        travelBook.setAccessUrl(au);
        travelBook.setAdminPassword(dto.getAdminPassword());
        travelBook.setCurrency(Currency.valueOf(dto.getCurrency()));
        travelBook.setAvaliable(true);
        travelBook.setCreateDateTime(LocalDateTime.now());
        
        rep.em().persist(au);
        rep.em().persist(travelBook);
        
        return travelBook;
    }

}
