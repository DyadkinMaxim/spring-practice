package org.springframework.samples.petclinic.rest.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.VisitPageDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/visits")
@CrossOrigin(exposedHeaders = "errors, content-type")
public class VisitRestControllerV2 {

    private final ClinicService clinicService;
    private final VisitMapper visitMapper;

    public VisitRestControllerV2(ClinicService clinicService, VisitMapper visitMapper) {
        this.clinicService = clinicService;
        this.visitMapper = visitMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    public ResponseEntity<VisitPageDto> listVisitsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<Visit> visits = this.clinicService.findVisitsPaged(
            PageRequest.of(page, size, Sort.by("id")));
        return ResponseEntity.ok(
            visitMapper.toVisitPageDto(visits)
        );
    }
}
