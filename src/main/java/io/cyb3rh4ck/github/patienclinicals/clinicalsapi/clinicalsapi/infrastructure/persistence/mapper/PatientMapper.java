package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PatientMapper {
    private final ClinicalMapper clinicalMapper;

    public PatientMapper(ClinicalMapper clinicalMapper) {
        this.clinicalMapper = clinicalMapper;
    }

    public Patient toDomain(PatientEntity entity) {
        if (entity == null) return null;
        
        Patient patient = new Patient();
        patient.setId(entity.getId());
        patient.setFirstName(entity.getFirstName());
        patient.setLastName(entity.getLastName());
        patient.setAge(entity.getAge());
        
        if (entity.getClinicals() != null) {
            patient.setClinicals(entity.getClinicals().stream()
                    .map(clinicalMapper::toDomain)
                    .collect(Collectors.toList()));
        }
        
        return patient;
    }

    public PatientEntity toEntity(Patient domain) {
        if (domain == null) return null;
        
        PatientEntity entity = new PatientEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setAge(domain.getAge());
        
        return entity;
    }
}