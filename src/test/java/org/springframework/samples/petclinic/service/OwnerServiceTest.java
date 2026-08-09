package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.rest.advice.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PetTypeRepository petTypeRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void addVisitToPetSuccess() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        PetType petType = new PetType();
        petType.setId(1);
        pet.setType(petType);
        owner.addPet(pet);

        when(ownerRepository.findById(anyInt())).thenReturn(owner);
        when(petRepository.findById(anyInt())).thenReturn(pet);

        clinicService.addVisitToPet(1, 1, new Visit());

        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void addVisitToPetInvalidPet() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        PetType petType = new PetType();
        petType.setId(1);
        pet.setType(petType);

        when(ownerRepository.findById(anyInt())).thenReturn(owner);
        when(petRepository.findById(anyInt())).thenReturn(pet);

        assertThrows(IllegalStateException.class,
            () -> clinicService.addVisitToPet(1111, 1, new Visit()));
    }

    @Test
    void addVisitToPet_PetNotFound() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        PetType petType = new PetType();
        petType.setId(1);
        pet.setType(petType);

        when(ownerRepository.findById(anyInt())).thenReturn(owner);
        when(petRepository.findById(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class,
            () -> clinicService.addVisitToPet(1111, 1, new Visit()));
    }


}
