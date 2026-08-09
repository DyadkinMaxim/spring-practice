package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.PetType;

public interface PetsByType {

    PetType getPetType();
    Integer getPetCount();
}
