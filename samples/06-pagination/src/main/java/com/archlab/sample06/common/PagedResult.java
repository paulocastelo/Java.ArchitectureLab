package com.archlab.sample06.common;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResult<T>(
        List<T> items,
        int page,
        int pageSize,
        long totalCount,
        int totalPages,
        boolean hasNextPage,
        boolean hasPreviousPage) {

    public static <T> PagedResult<T> from(Page<T> springPage) {
        return new PagedResult<>(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.hasNext(),
                springPage.hasPrevious());
    }
}
