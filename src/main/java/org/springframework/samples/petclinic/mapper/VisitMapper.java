package org.springframework.samples.petclinic.mapper;

import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.rest.dto.VetPageDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.samples.petclinic.rest.dto.VisitPageDto;

import java.util.Collection;
import java.util.List;

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(uses = PetMapper.class)
public interface VisitMapper {
    @Mapping(source = "petId", target = "pet.id")
    Visit toVisit(VisitDto visitDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    Visit toVisit(VisitFieldsDto visitFieldsDto);

    Collection<Visit> toVisits(Collection<VisitDto> visits);

    @Mapping(source = "pet.id", target = "petId")
    VisitDto toVisitDto(Visit visit);

    List<VisitDto> toVisitDtoCollection(Collection<Visit> visitCollection);

    Collection<VisitDto> toVisitsDto(Collection<Visit> visits);

    default VisitPageDto toVisitPageDto(@NonNull Page<Visit> visitPage) {
        return new VisitPageDto(
            toVisitDtoCollection(visitPage.getContent()),
            visitPage.getNumber(),
            visitPage.getSize(),
            visitPage.getTotalElements(),
            visitPage.getTotalPages()
        );
    }
}
