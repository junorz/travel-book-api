package com.junorz.travelbook.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class SecondaryCategory {
    
    @Id
    @GeneratedValue(generator = "scIdGen")
    @GenericGenerator(name = "scIdGen", strategy = "native")
    private long id;
    
    @NotNull
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category_id", referencedColumnName = "id")
    private PrimaryCategory primaryCategory;
    
}
