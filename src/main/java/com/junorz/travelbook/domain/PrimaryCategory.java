package com.junorz.travelbook.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class PrimaryCategory implements Serializable {

    private static final long serialVersionUID = 2546266388267883466L;

    @Id
    @GeneratedValue(generator = "pcIdGen")
    @GenericGenerator(name = "pcIdGen", strategy = "native")
    private long id;

    @NotNull
    private String name;
    
    @OneToMany(mappedBy = "primaryCategory", cascade = CascadeType.ALL)
    private List<SecondaryCategory> secondaryCategoryList = new ArrayList<>();
    
    public static Optional<PrimaryCategory> findById(long id, Repository rep) {
        return Optional.ofNullable(rep.em().find(PrimaryCategory.class, id));
    }

}
