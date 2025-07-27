package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.adapter;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.PatientRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper.PatientMapper;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaPatientRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PatientRepositoryAdapter implements PatientRepository {
    private final JpaPatientRepository jpaPatientRepository;
    private final PatientMapper patientMapper;

    public PatientRepositoryAdapter(JpaPatientRepository jpaPatientRepository, PatientMapper patientMapper) {
        this.jpaPatientRepository = jpaPatientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public Patient save(Patient patient) {
        return patientMapper.toDomain(
                jpaPatientRepository.save(patientMapper.toEntity(patient)));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return jpaPatientRepository.findById(id)
                .map(patientMapper::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return jpaPatientRepository.findAll().stream()
                .map(patientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaPatientRepository.deleteById(id);
    }

    @Override
    public List<Patient> findByLastName(String lastName) {
        return jpaPatientRepository.findByLastName(lastName).stream()
                .map(patientMapper::toDomain)
                .collect(Collectors.toList());
    }
}