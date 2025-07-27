package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import java.util.List;
import java.util.Optional;

public interface ClinicalRepository {
    Clinical save(Clinical clinical);
    Optional<Clinical> findById(Long id);
    List<Clinical> findAll();
    List<Clinical> findByPatientId(Long patientId);
    void deleteById(Long id);
}