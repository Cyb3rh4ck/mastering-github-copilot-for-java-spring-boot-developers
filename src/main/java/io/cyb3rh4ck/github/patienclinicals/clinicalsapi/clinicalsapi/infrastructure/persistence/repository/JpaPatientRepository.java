package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaPatientRepository extends JpaRepository<PatientEntity, Long> {
    List<PatientEntity> findByLastName(String lastName);
}