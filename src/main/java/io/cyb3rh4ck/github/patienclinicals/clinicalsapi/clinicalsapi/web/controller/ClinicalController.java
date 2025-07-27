package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.web.controller;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service.ClinicalService;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clinicals")
@CrossOrigin(origins = "*")
public class ClinicalController {
    private final ClinicalService clinicalService;

    public ClinicalController(ClinicalService clinicalService) {
        this.clinicalService = clinicalService;
    }

    @PostMapping
    public ResponseEntity<Clinical> createClinical(@RequestBody Clinical clinical) {
        try {
            Clinical createdClinical = clinicalService.createClinical(clinical);
            return new ResponseEntity<>(createdClinical, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Clinical>> getAllClinicals() {
        List<Clinical> clinicals = clinicalService.getAllClinicals();
        return ResponseEntity.ok(clinicals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinical> getClinicalById(@PathVariable Long id) {
        return clinicalService.getClinicalById(id)
                .map(clinical -> ResponseEntity.ok(clinical))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Clinical>> getClinicalsByPatientId(@PathVariable Long patientId) {
        List<Clinical> clinicals = clinicalService.getClinicalsByPatientId(patientId);
        return ResponseEntity.ok(clinicals);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinical> updateClinical(@PathVariable Long id, @RequestBody Clinical clinical) {
        try {
            Clinical updatedClinical = clinicalService.updateClinical(id, clinical);
            return ResponseEntity.ok(updatedClinical);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinical(@PathVariable Long id) {
        clinicalService.deleteClinical(id);
        return ResponseEntity.noContent().build();
    }
}