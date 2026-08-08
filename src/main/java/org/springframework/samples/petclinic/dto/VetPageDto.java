package org.springframework.samples.petclinic.dto;

import org.springframework.samples.petclinic.rest.dto.OwnerDto;

import java.util.List;

public record VetPageDto (
    List<OwnerDto> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages
) {
}
