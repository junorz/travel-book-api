package com.junorz.travelbook.context.dto;

import com.junorz.travelbook.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private String id;
    private String name;

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getName());
    }
}
