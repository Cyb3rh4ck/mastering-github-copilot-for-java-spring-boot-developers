package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        // Business logic validation
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty() || 
            patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name and last name are required");
        }
        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    existingPatient.setFirstName(updatedPatient.getFirstName());
                    existingPatient.setLastName(updatedPatient.getLastName());
                    existingPatient.setAge(updatedPatient.getAge());
                    return patientRepository.save(existingPatient);
                })
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}