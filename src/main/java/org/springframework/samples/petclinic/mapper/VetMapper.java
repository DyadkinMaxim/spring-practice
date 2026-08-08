package org.springframework.samples.petclinic.mapper;

import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.samples.petclinic.rest.dto.VetPageDto;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.rest.dto.VetFieldsDto;

import java.util.Collection;
import java.util.List;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    @Mapping(target = "id", ignore = true)
    Vet toVet(VetFieldsDto vetFieldsDto);

    Collection<Vet> toVets(Collection<VetDto> vets);

    VetDto toVetDto(Vet vet);

    List<VetDto> toVetDtoCollection(Collection<Vet> vetCollection);

    Collection<VetDto> toVetDtos(Collection<Vet> vets);

    default VetPageDto toVetPageDto(@NonNull Page<Vet> vetPage) {
        return new VetPageDto(
            toVetDtoCollection(vetPage.getContent()),
            vetPage.getNumber(),
            vetPage.getSize(),
            vetPage.getTotalElements(),
            vetPage.getTotalPages()
        );
    }
}
