package com.junorz.travelbook.domain;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.SettleCreateDto;
import com.junorz.travelbook.context.exception.ResourceNotFoundException;
import com.junorz.travelbook.context.orm.Repository;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "settle", indexes = {@Index(name = "travelbook_id", columnList = "travelbook_id")})
public class Settle implements Serializable {

    private static final long serialVersionUID = 136367672921917538L;

    @Id
    @GeneratedValue(generator = "settleIdGen")
    @GenericGenerator(name = "settleIdGen", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelbook_id", referencedColumnName = "id")
    private TravelBook travelBook;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", referencedColumnName = "id")
    private Member fromMember;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", referencedColumnName = "id")
    private Member toMember;

    @NotNull
    private BigDecimal amount;


    public static List<Settle> findAll(String travelBookId, Repository rep) {
        return rep.em().createQuery("SELECT s FROM Settle s WHERE s.travelBook = ?1", Settle.class)
                .setParameter(1, TravelBook.findById(travelBookId, rep).orElseThrow(() -> new ResourceNotFoundException(Messages.NO_TRAVELBOOK_FOUND)))
                .getResultList();
    }


    public static Settle create(SettleCreateDto dto, Repository rep) {
        TravelBook travelBook = TravelBook.findById(dto.getTravelBookId(), rep).orElseThrow(() -> new ResourceNotFoundException(Messages.NO_TRAVELBOOK_FOUND));
        Member fromMember = Member.findById(dto.getFromMemberId(), rep).orElseThrow(() -> new ResourceNotFoundException(Messages.MEMBER_NOT_FOUND));
        Member toMember = Member.findById(dto.getToMemberId(), rep).orElseThrow(() -> new ResourceNotFoundException(Messages.MEMBER_NOT_FOUND));
        BigDecimal amount = new BigDecimal(dto.getAmount());

        Settle settle = new Settle();
        settle.setTravelBook(travelBook);
        settle.setFromMember(fromMember);
        settle.setToMember(toMember);
        settle.setAmount(amount);
        rep.em().persist(settle);

        return settle;
    }


}
