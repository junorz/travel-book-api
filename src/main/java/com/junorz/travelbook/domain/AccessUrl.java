package com.junorz.travelbook.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class AccessUrl implements Serializable {

    
    private static final long serialVersionUID = 6271955356315267961L;
    
    @Id
    private String url;
    
    public static AccessUrl findByUrl(String url, Repository rep) {
        return rep.em().find(AccessUrl.class, url);
    }

}
