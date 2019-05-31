package com.junorz.travelbook.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.orm.Repository;

import lombok.Data;

@Entity
@Data
public class Member implements Serializable {


    private static final long serialVersionUID = 2035985112426114136L;

    @Id
    @GeneratedValue(generator = "memberIdGen")
    @GenericGenerator(name = "memberIdGen", strategy = "uuid2")
    private String id;

    @NotNull
    private String name;
    
    @NotNull
    private boolean isAvaliable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;
    
    public static Optional<Member> findById(String id, Repository rep) { 
        return Optional.ofNullable(rep.em().find(Member.class, id));
    }

    public static Optional<Member> create(MemberCreateDto dto, Repository rep) {
        Optional<TravelBook> travelBookOpt = TravelBook.findById(dto.getTravelBookId(), rep);
        return travelBookOpt.map(t -> {
            Member member = new Member();
            member.setName(dto.getMemberName());
            member.setTravelBook(t);
            rep.em().persist(member);
            return member;
        });
    }
    
    public static Optional<Member> edit(String memberId, MemberCreateDto dto, Repository rep) {
        Optional<Member> memberOpt = findById(memberId, rep);
        memberOpt.ifPresent(member -> {
            member.setName(dto.getMemberName());
            rep.em().merge(member);
        });
        return memberOpt;
    }
    
    public static Optional<Member> delete(String memberId, Repository rep) {
        Optional<Member> memberOpt = Member.findById(memberId, rep);
        memberOpt.ifPresent(member -> member.setAvaliable(false));
        return memberOpt;
    }
}
