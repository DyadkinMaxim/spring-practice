package org.springframework.samples.petclinic.rest.advice;

public class NotFoundException extends RuntimeException {
    public NotFoundException(int id) {
        super(String.format("Not found entity with id: %s", id));
    }
}
