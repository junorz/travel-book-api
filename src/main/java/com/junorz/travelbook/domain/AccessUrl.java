package com.junorz.travelbook.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class AccessUrl {

    @Id
    private String url;

    @OneToOne
    @JoinColumn(name = "travelBook_id", referencedColumnName = "id")
    @JsonBackReference
    private TravelBook travelBook;

    public static AccessUrl findByUrl(String url, Repository rep) {
        List<AccessUrl> accessUrlList = rep.em()
                .createQuery("SELECT au FROM AccessUrl au WHERE au.url = ?1", AccessUrl.class).setParameter(1, url)
                .getResultList();
        return accessUrlList.size() > 0 ? accessUrlList.get(0) : null;
    }

}
