package com.junorz.travelbook.domain;

import com.junorz.travelbook.context.orm.Repository;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Data
public class SecondaryCategory implements Serializable {
    
    private static final long serialVersionUID = 667566355194417815L;

    @Id
    @GeneratedValue(generator = "scIdGen")
    @GenericGenerator(name = "scIdGen", strategy = "native")
    private long id;
    
    @NotNull
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category_id", referencedColumnName = "id")
    private PrimaryCategory primaryCategory;

    public static SecondaryCategory of(String name, PrimaryCategory primaryCategory) {
        SecondaryCategory secondaryCategory = new SecondaryCategory();
        secondaryCategory.setName(name);
        secondaryCategory.setPrimaryCategory(primaryCategory);
        return secondaryCategory;
    }
    
    public static Optional<SecondaryCategory> findById(long id, Repository rep) {
        return Optional.ofNullable(rep.em().find(SecondaryCategory.class, id));
    }
    
}
