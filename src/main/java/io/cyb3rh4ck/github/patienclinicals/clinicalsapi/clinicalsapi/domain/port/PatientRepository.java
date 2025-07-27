package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    List<Patient> findAll();
    void deleteById(Long id);
    List<Patient> findByLastName(String lastName);
}