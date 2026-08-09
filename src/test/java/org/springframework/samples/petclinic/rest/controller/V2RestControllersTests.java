package org.springframework.samples.petclinic.rest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.controller.v2.OwnerRestControllerV2;
import org.springframework.samples.petclinic.rest.controller.v2.PetRestControllerV2;
import org.springframework.samples.petclinic.rest.controller.v2.VetRestControllerV2;
import org.springframework.samples.petclinic.rest.controller.v2.VisitRestControllerV2;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class V2RestControllersTests {
    @Autowired
    private OwnerRestControllerV2 ownerRestControllerV2;

    @Autowired
    private PetRestControllerV2 petRestControllerV2;

    @Autowired
    private VetRestControllerV2 vetRestControllerV2;

    @Autowired
    private VisitRestControllerV2 visitRestControllerV2;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private VetMapper vetMapper;

    @Autowired
    private VisitMapper visitMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<OwnerDto> owners;

    private List<PetDto> pets;

    private List<VetDto> vets;

    private List<VisitDto> visits;

    @BeforeEach
    void initOwners() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
            ownerRestControllerV2,
                petRestControllerV2,
                vetRestControllerV2,
                visitRestControllerV2
                )
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        owners = new ArrayList<>();

        OwnerDto ownerWithPet = new OwnerDto();
        owners.add(ownerWithPet.id(1).firstName("George").lastName("Franklin").address("110 W. Liberty St.").city("Madison").telephone("6085551023"));
        OwnerDto owner = new OwnerDto();
        owners.add(owner.id(2).firstName("Betty").lastName("Davis").address("638 Cardinal Ave.").city("Sun Prairie").telephone("6085551749"));
        owner = new OwnerDto();
        owners.add(owner.id(3).firstName("Eduardo").lastName("Rodriquez").address("2693 Commerce St.").city("McFarland").telephone("6085558763"));
        owner = new OwnerDto();
        owners.add(owner.id(4).firstName("Harold").lastName("Davis").address("563 Friendly St.").city("Windsor").telephone("6085553198"));

        PetTypeDto petType = new PetTypeDto();
        petType.id(2)
            .name("dog");

        pets = new ArrayList<>();
        PetDto pet = new PetDto();
        pets.add(pet.id(3)
            .name("Rosy")
            .birthDate(LocalDate.now())
            .type(petType));

        pet = new PetDto();
        pets.add(pet.id(4)
            .name("Jewel")
            .birthDate(LocalDate.now())
            .type(petType));

        vets = new ArrayList<>();
        VetDto vet = new VetDto();
        SpecialtyDto specialty = new SpecialtyDto(1, "specialty1");
        vets.add(vet.id(1)
            .firstName("Name1")
            .specialties(new ArrayList<>(List.of(specialty))));

        vet = new VetDto();
        vets.add(vet.id(2)
            .firstName("Name2")
            .specialties(new ArrayList<>(List.of(specialty))));

        visits = new ArrayList<>();
        VisitDto visitDto = new VisitDto();
        visits.add(visitDto.id(1)
            .description("desc1")
            .petId(1));

        visitDto = new VisitDto();
        visits.add(visitDto.id(2)
            .description("desc2")
            .petId(2));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 2, Sort.by("id"));
        var pageOwners = ownerMapper.toOwners(owners.subList(0, 2)).stream().toList();
        given(this.clinicService.findOwnersByCriteria(null, pageRequest))
            .willReturn(new PageImpl<>(pageOwners, pageRequest, owners.size()));
        this.mockMvc.perform(get("/api/v2/owners?page=0&size=2")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].firstName").value("George"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].firstName").value("Betty"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.totalElements").value(4))
            .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetsPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 5, Sort.by("id"));
        var pagePets = petMapper.toPets(pets).stream().toList();
        given(this.clinicService.findPets(pageRequest))
            .willReturn(new PageImpl<>(pagePets, pageRequest, pets.size()));
        this.mockMvc.perform(get("/api/v2/pets?page=0&size=5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(3))
            .andExpect(jsonPath("$.content[0].name").value("Rosy"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(5))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetVetsPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 5, Sort.by("id"));
        var vetsPage = vetMapper.toVets(vets).stream().toList();
        given(this.clinicService.findVetsPaged(any(Pageable.class)))
            .willReturn(new PageImpl<>(vetsPage, pageRequest, vets.size()));
        this.mockMvc.perform(get("/api/v2/vets")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].firstName").value("Name1"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(5))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetVisitsPageSuccess() throws Exception {
        var pageRequest = PageRequest.of(0, 5, Sort.by("id"));
        var visitsPage = visitMapper.toVisits(visits).stream().toList();
        given(this.clinicService.findVisitsPaged(any(Pageable.class)))
            .willReturn(new PageImpl<>(visitsPage, pageRequest, visits.size()));
        this.mockMvc.perform(get("/api/v2/visits")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].description").value("desc1"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(5))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(1));
    }
}
