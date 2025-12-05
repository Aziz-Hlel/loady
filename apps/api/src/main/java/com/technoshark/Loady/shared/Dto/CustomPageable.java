package com.technoshark.Loady.shared.Dto;

public record CustomPageable(
        int number,
        int size,
        long offset,
        int pageSize,
        long totalElements,
        int totalPages) {
}
