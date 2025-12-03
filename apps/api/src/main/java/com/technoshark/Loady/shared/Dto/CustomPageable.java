package com.technoshark.Loady.shared.Dto;

public record CustomPageable(
        int number,
        int size,
        long totalElements,
        int totalPages) {
}
