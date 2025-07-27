package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.ClinicalRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicalService {
    private final ClinicalRepository clinicalRepository;
    private final PatientRepository patientRepository;

    public ClinicalService(ClinicalRepository clinicalRepository, PatientRepository patientRepository) {
        this.clinicalRepository = clinicalRepository;
        this.patientRepository = patientRepository;
    }

    public Clinical createClinical(Clinical clinical) {
        // Validate patient exists
        if (!patientRepository.findById(clinical.getPatientId()).isPresent()) {
            throw new IllegalArgumentException("Patient not found");
        }
        return clinicalRepository.save(clinical);
    }

    public Optional<Clinical> getClinicalById(Long id) {
        return clinicalRepository.findById(id);
    }

    public List<Clinical> getAllClinicals() {
        return clinicalRepository.findAll();
    }

    public List<Clinical> getClinicalsByPatientId(Long patientId) {
        return clinicalRepository.findByPatientId(patientId);
    }

    public Clinical updateClinical(Long id, Clinical updatedClinical) {
        return clinicalRepository.findById(id)
                .map(existingClinical -> {
                    existingClinical.setComponentName(updatedClinical.getComponentName());
                    existingClinical.setComponentValue(updatedClinical.getComponentValue());
                    existingClinical.setMeasuredDateTime(updatedClinical.getMeasuredDateTime());
                    return clinicalRepository.save(existingClinical);
                })
                .orElseThrow(() -> new RuntimeException("Clinical not found"));
    }

    public void deleteClinical(Long id) {
        clinicalRepository.deleteById(id);
    }
}