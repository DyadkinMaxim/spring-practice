package org.springframework.samples.petclinic.rest.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.VetPageDto;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/v2/vets")
public class VetRestControllerV2 {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;

    public VetRestControllerV2(ClinicService clinicService, VetMapper vetMapper) {
        this.clinicService = clinicService;
        this.vetMapper = vetMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    public ResponseEntity<VetPageDto> listVetsPage(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size) {
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 20 : size;
        Page<Vet> vets = this.clinicService.findVetsPaged(
            PageRequest.of(pageNumber, pageSize, Sort.by("id")));
        return new ResponseEntity<>(vetMapper.toVetPageDto(vets), HttpStatus.OK);
    }
}

