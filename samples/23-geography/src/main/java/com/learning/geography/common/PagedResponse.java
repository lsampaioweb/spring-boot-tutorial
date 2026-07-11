package com.learning.geography.common;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param items         the items on the current page
 * @param page          zero-based page index
 * @param size          number of items requested per page
 * @param totalElements total number of items across all pages
 * @param totalPages    total number of pages available
 */
public record PagedResponse<T>(
    List<T> items,
    int page,
    int size,
    long totalElements,
    int totalPages) {
}
