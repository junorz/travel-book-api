package com.junorz.travelbook.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class PrimaryCategory {

    @Id
    @GeneratedValue(generator = "pcIdGen")
    @GenericGenerator(name = "pcIdGen", strategy = "native")
    private long id;

    @NotNull
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryCategory")
    private List<SecondaryCategory> secondaryCategoryList;

}
