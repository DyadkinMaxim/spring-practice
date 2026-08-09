package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void transferPet_success() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Name1");
        Owner oldOwner = new Owner();
        oldOwner.setId(2);
        pet.setOwner(oldOwner);
        Owner newOwner = new Owner();
        newOwner.setId(4);

        when(petRepository.findById(anyInt())).thenReturn(pet);
        when(ownerRepository.findById(anyInt())).thenReturn(newOwner);

        clinicService.petTransfer(1, 4);
        assertEquals(pet.getOwner().getId(), newOwner.getId());
    }

    @Test
    void transferPet_ownerNotFound() {
        when(petRepository.findById(anyInt())).thenReturn(new Pet());
        when(ownerRepository.findById(anyInt())).thenReturn(null);

        assertThrows(IllegalStateException.class,
            () -> clinicService.petTransfer(1, 4));
    }

    @Test
    void transferPet_petNotFound() {
        when(petRepository.findById(anyInt())).thenReturn(null);
        when(ownerRepository.findById(anyInt())).thenReturn(new Owner());

        assertThrows(IllegalStateException.class,
            () -> clinicService.petTransfer(1, 4));
    }
}

