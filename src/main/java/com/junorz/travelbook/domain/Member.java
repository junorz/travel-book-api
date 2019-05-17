package com.junorz.travelbook.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;

    public static Member create(String travelBookId, String memberName, Repository rep) {
        List<TravelBook> travelBookList = rep.em()
                .createQuery("SELECT tb FROM TravelBook tb WHERE tb.id = ?1", TravelBook.class)
                .setParameter(1, travelBookId).getResultList();
        if (travelBookList.size() > 0) {
            TravelBook travelBook = travelBookList.get(0);
            Member member = new Member();
            member.setName(memberName);
            member.setTravelBook(travelBook);
            rep.em().persist(member);
            return member;
        }
        return null;
    }
}
