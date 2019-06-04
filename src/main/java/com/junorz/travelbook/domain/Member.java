package com.junorz.travelbook.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.MemberCreateDto;
import com.junorz.travelbook.context.exception.InvalidOperationException;
import com.junorz.travelbook.context.exception.ResourceNotFoundException;
import com.junorz.travelbook.context.exception.ValidationException;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;
    
    private boolean isAvaliable = true;
    
    public static Optional<Member> findById(String id, Repository rep) { 
        return Optional.ofNullable(rep.em().find(Member.class, id));
    }

    public static Optional<Member> create(MemberCreateDto dto, Repository rep) {
        Optional<TravelBook> travelBookOpt = TravelBook.findById(dto.getTravelBookId(), rep);
        return travelBookOpt.map(t -> {
            // prevent duplicate member name in a travel book.
            t.getMemeberList().forEach(m -> {
                if (dto.getMemberName().equals(m.getName())) {
                    throw new ValidationException(Messages.MEMBER_NAME_DUPLICATE);
                }
            });
            
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
        // Check if this member has any payment information.
        // The payment information related to this member must be removed first.
        Member member = memberOpt.orElseThrow(() -> new ResourceNotFoundException(Messages.MEMBER_NOT_FOUND));
        member.getTravelBook().getDetailList().forEach(d -> {
            if (d.isAvaliable()) {
                if (memberId.equals(d.getMember().getId())) {
                    throw new InvalidOperationException(Messages.MEMBER_RELATED_TO_DETAILS);
                }
                d.getMemberList().forEach(m -> {
                    if (memberId.equals(m.getId())) {
                        throw new InvalidOperationException(Messages.MEMBER_RELATED_TO_DETAILS);
                    }
                });
            }
        });
        
        memberOpt.ifPresent(m -> {
            StringBuilder newName = new StringBuilder();
            newName.append(m.getName());
            newName.append("_");
            newName.append(Instant.now().getEpochSecond());
            m.setName(newName.toString());
            m.setAvaliable(false);
            rep.em().merge(m);
        });
        return memberOpt;
    }
}
