package com.junorz.travelbook.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.context.dto.TravelBookCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.utils.AccessUrlUtil;

import lombok.Data;

@Entity
@Data
public class TravelBook {

    @Id
    @GeneratedValue(generator = "travelBookIdGen")
    @GenericGenerator(name = "travelBookIdGen", strategy = "uuid2")
    private String id;

    @NotNull
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "access_url", referencedColumnName = "url")
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
        return rep.em().createQuery("SELECT tb FROM TravelBook tb WHERE tb.isAvaliable = true", TravelBook.class)
                .getResultList();
    }

    public static TravelBook findById(String id, Repository rep) {
        TravelBook travelBook = rep.em().find(TravelBook.class, id);
        return travelBook.isAvaliable() ? travelBook : null;
    }
    
    public static TravelBook findByUrl(String url, Repository rep) {
        List<TravelBook> travelBookList = rep.em().createQuery("SELECT tb FROM TravelBook tb WHERE tb.accessUrl = ?1", TravelBook.class)
                .setParameter(1, AccessUrl.findByUrl(url, rep))
                .getResultList();
        return travelBookList.size() == 0 ? null : travelBookList.get(0);
    }

    public static TravelBook create(TravelBookCreateDto dto, Repository rep) {
        // trying to create an unique access url.
        String accessUrl = AccessUrlUtil.getUrl();
        boolean isAccessUrlAvaliable = AccessUrlUtil.checkUrlUnique(accessUrl, rep);
        if (!isAccessUrlAvaliable) {
            int retry = 0;
            // try 10 times
            for (int i = 0; i < 10; i++) {
                accessUrl = AccessUrlUtil.getUrl();
                if (AccessUrlUtil.checkUrlUnique(accessUrl, rep))
                    break;
                retry++;
            }
            if (retry == 10) {
                // throw an exception and roll back.
                throw new RuntimeException("Access url creation failed: too many attempts.");
            }
        }
        TravelBook travelBook = new TravelBook();
        AccessUrl au = new AccessUrl();
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
    
    public static TravelBook edit(String id, String name, String adminPassword, String currency, Repository rep) {
        TravelBook travelBook = TravelBook.findById(id, rep);
        travelBook.setName(name);
        if (StringUtils.isNotEmpty(adminPassword)) {
            // The password is already encoded in service layer
            travelBook.setAdminPassword(adminPassword);
        }
        travelBook.setCurrency(Currency.valueOf(currency));
        rep.em().merge(travelBook);
        return travelBook;
    }

    public static TravelBook delete(String id, Repository rep) {
        TravelBook travelBook = TravelBook.findById(id, rep);
        travelBook.setAvaliable(false);
        rep.em().merge(travelBook);
        return travelBook;
    }

}
