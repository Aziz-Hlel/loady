package com.technoshark.Loady.shared.Dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record CustomPage<T>(List<T> content, CustomPageable pagination) {

    public static <T> CustomPage<T> from(Page<T> page) {
        return new CustomPage<>(
                page.getContent(),
                new CustomPageable(
                        page.getNumber(),
                        page.getSize(),
                        page.getPageable().getOffset(),
                        page.getPageable().getPageSize(),
                        page.getTotalElements(),
                        page.getTotalPages()));
    }

}
