package org.springframework.samples.petclinic.rest.dto;

import java.util.List;

public record VisitPageDto (
    List<VisitDto> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages
) {}
