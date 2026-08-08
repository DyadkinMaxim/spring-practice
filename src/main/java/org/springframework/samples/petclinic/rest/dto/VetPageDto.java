package org.springframework.samples.petclinic.rest.dto;

import org.springframework.samples.petclinic.rest.dto.OwnerDto;

import java.util.List;

public record VetPageDto (
    List<VetDto> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages
) {
}
