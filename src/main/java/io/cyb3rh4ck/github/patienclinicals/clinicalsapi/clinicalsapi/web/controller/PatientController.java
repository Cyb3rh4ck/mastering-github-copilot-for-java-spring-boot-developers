package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.web.controller;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service.PatientService;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        log.info("Creating new patient: {}", patient != null ? patient.getFirstName() + " " + patient.getLastName() : "null");
        if (patient == null) {
            log.warn("Attempt to create patient with null data");
            return ResponseEntity.badRequest().body(null);
        }
        Patient createdPatient = patientService.createPatient(patient);
        log.info("Successfully created patient with ID: {}", createdPatient.getId());
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        log.info("Retrieving all patients");
        List<Patient> patients = patientService.getAllPatients();
        log.info("Retrieved {} patients", patients.size());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        log.info("Retrieving patient with ID: {}", id);
        if (id == null || id <= 0) {
            log.warn("Invalid patient ID provided: {}", id);
            return ResponseEntity.badRequest().build();
        }
        return patientService.getPatientById(id)
                .map(patient -> {
                    log.info("Found patient with ID: {}", id);
                    return ResponseEntity.ok(patient);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Patient>> getPatientsByLastName(@PathVariable String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Patient> patients = patientService.getPatientsByLastName(lastName);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        if (id == null || id <= 0 || patient == null) {
            return ResponseEntity.badRequest().build();
        }
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.info("Deleting patient with ID: {}", id);
        if (id == null || id <= 0) {
            log.warn("Invalid patient ID for deletion: {}", id);
            return ResponseEntity.badRequest().build();
        }
        patientService.deletePatient(id);
        log.info("Successfully deleted patient with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // Global exception handlers for this controller
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException occurred: {}", e.getMessage());
        if ("Patient not found".equals(e.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
    }
}