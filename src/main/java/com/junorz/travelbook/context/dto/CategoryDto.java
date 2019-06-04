package com.junorz.travelbook.context.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.junorz.travelbook.domain.PrimaryCategory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {

    private long id;
    private String name;
    private List<SecondaryCategory> secondaryCategoryList;

    @Data
    @AllArgsConstructor
    private static class SecondaryCategory {
        private long id;
        private String name;
    }

    public static CategoryDto of(PrimaryCategory primaryCategory) {
        return new CategoryDto(primaryCategory.getId(), primaryCategory.getName(), primaryCategory.getSecondaryCategoryList().stream()
                .map(s -> new SecondaryCategory(s.getId(), s.getName())).collect(Collectors.toList()));
    }
}
