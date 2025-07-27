package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.adapter;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.ClinicalRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper.ClinicalMapper;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaClinicalRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaPatientRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClinicalRepositoryAdapter implements ClinicalRepository {
    private final JpaClinicalRepository jpaClinicalRepository;
    private final JpaPatientRepository jpaPatientRepository;
    private final ClinicalMapper clinicalMapper;

    public ClinicalRepositoryAdapter(JpaClinicalRepository jpaClinicalRepository, 
                                   JpaPatientRepository jpaPatientRepository,
                                   ClinicalMapper clinicalMapper) {
        this.jpaClinicalRepository = jpaClinicalRepository;
        this.jpaPatientRepository = jpaPatientRepository;
        this.clinicalMapper = clinicalMapper;
    }

    @Override
    public Clinical save(Clinical clinical) {
        var entity = clinicalMapper.toEntity(clinical);
        if (clinical.getPatientId() != null) {
            PatientEntity patient = jpaPatientRepository.findById(clinical.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            entity.setPatient(patient);
        }
        return clinicalMapper.toDomain(jpaClinicalRepository.save(entity));
    }

    @Override
    public Optional<Clinical> findById(Long id) {
        return jpaClinicalRepository.findById(id)
                .map(clinicalMapper::toDomain);
    }

    @Override
    public List<Clinical> findAll() {
        return jpaClinicalRepository.findAll().stream()
                .map(clinicalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Clinical> findByPatientId(Long patientId) {
        return jpaClinicalRepository.findByPatientId(patientId).stream()
                .map(clinicalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaClinicalRepository.deleteById(id);
    }
}