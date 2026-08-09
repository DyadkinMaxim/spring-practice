package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuditTest {

    @MockitoBean
    OwnerRepository ownerRepository;

    @Autowired
    ClinicServiceImpl clinicService;

    @Test
    void testSaveOwnerAudit() {
        assertDoesNotThrow(() -> clinicService.saveOwner(new Owner()));
    }
}
