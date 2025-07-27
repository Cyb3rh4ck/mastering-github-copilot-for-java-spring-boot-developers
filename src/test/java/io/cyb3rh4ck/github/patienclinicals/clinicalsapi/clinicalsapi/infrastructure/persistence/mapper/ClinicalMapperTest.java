package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.ClinicalEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClinicalMapperTest {

    private ClinicalMapper clinicalMapper;
    private Clinical clinical;
    private ClinicalEntity clinicalEntity;
    private PatientEntity patientEntity;

    @BeforeEach
    void setUp() {
        clinicalMapper = new ClinicalMapper();

        clinical = new Clinical();
        clinical.setId(1L);
        clinical.setPatientId(123L);
        clinical.setComponentName("Blood Pressure");
        clinical.setComponentValue("120/80");
        clinical.setMeasuredDateTime(LocalDateTime.of(2025, 1, 15, 10, 30));

        patientEntity = new PatientEntity();
        patientEntity.setId(123L);
        patientEntity.setFirstName("John");
        patientEntity.setLastName("Doe");
        patientEntity.setAge(30);

        clinicalEntity = new ClinicalEntity();
        clinicalEntity.setId(1L);
        clinicalEntity.setPatient(patientEntity);
        clinicalEntity.setComponentName("Blood Pressure");
        clinicalEntity.setComponentValue("120/80");
        clinicalEntity.setMeasuredDateTime(Timestamp.valueOf(LocalDateTime.of(2025, 1, 15, 10, 30)));
    }

    @Test
    void testToDomain_WithValidEntity() {
        // When
        Clinical result = clinicalMapper.toDomain(clinicalEntity);

        // Then
        assertNotNull(result);
        assertEquals(clinicalEntity.getId(), result.getId());
        assertEquals(patientEntity.getId(), result.getPatientId());
        assertEquals(clinicalEntity.getComponentName(), result.getComponentName());
        assertEquals(clinicalEntity.getComponentValue(), result.getComponentValue());
        assertEquals(clinicalEntity.getMeasuredDateTime().toLocalDateTime(), result.getMeasuredDateTime());
    }

    @Test
    void testToDomain_WithNullPatient() {
        // Given
        clinicalEntity.setPatient(null);

        // When
        Clinical result = clinicalMapper.toDomain(clinicalEntity);

        // Then
        assertNotNull(result);
        assertEquals(clinicalEntity.getId(), result.getId());
        assertNull(result.getPatientId());
        assertEquals(clinicalEntity.getComponentName(), result.getComponentName());
        assertEquals(clinicalEntity.getComponentValue(), result.getComponentValue());
        assertEquals(clinicalEntity.getMeasuredDateTime().toLocalDateTime(), result.getMeasuredDateTime());
    }

    @Test
    void testToDomain_WithNullEntity() {
        // When
        Clinical result = clinicalMapper.toDomain(null);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_WithValidDomain() {
        // When
        ClinicalEntity result = clinicalMapper.toEntity(clinical);

        // Then
        assertNotNull(result);
        assertEquals(clinical.getId(), result.getId());
        assertEquals(clinical.getComponentName(), result.getComponentName());
        assertEquals(clinical.getComponentValue(), result.getComponentValue());
        assertEquals(Timestamp.valueOf(clinical.getMeasuredDateTime()), result.getMeasuredDateTime());
        // Note: Patient is not set in the mapper, it's handled in the adapter
        assertNull(result.getPatient());
    }

    @Test
    void testToEntity_WithNullDomain() {
        // When
        ClinicalEntity result = clinicalMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_WithMinimalData() {
        // Given
        Clinical minimalClinical = new Clinical();
        minimalClinical.setComponentName("Temperature");
        minimalClinical.setComponentValue("98.6°F");

        // When
        ClinicalEntity result = clinicalMapper.toEntity(minimalClinical);

        // Then
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(minimalClinical.getComponentName(), result.getComponentName());
        assertEquals(minimalClinical.getComponentValue(), result.getComponentValue());
        assertNull(result.getMeasuredDateTime());
        assertNull(result.getPatient());
    }

    @Test
    void testToDomain_WithMinimalEntity() {
        // Given
        ClinicalEntity minimalEntity = new ClinicalEntity();
        minimalEntity.setComponentName("Temperature");
        minimalEntity.setComponentValue("98.6°F");

        // When
        Clinical result = clinicalMapper.toDomain(minimalEntity);

        // Then
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getPatientId());
        assertEquals(minimalEntity.getComponentName(), result.getComponentName());
        assertEquals(minimalEntity.getComponentValue(), result.getComponentValue());
        assertNull(result.getMeasuredDateTime());
    }

    @Test
    void testRoundTripMapping() {
        // Given
        Clinical originalClinical = new Clinical();
        originalClinical.setId(1L);
        originalClinical.setPatientId(123L);
        originalClinical.setComponentName("Heart Rate");
        originalClinical.setComponentValue("72 bpm");
        originalClinical.setMeasuredDateTime(LocalDateTime.now());

        // When
        ClinicalEntity entity = clinicalMapper.toEntity(originalClinical);
        // Simulate setting the patient in the adapter
        PatientEntity patient = new PatientEntity();
        patient.setId(123L);
        entity.setPatient(patient);
        
        Clinical resultClinical = clinicalMapper.toDomain(entity);

        // Then
        assertEquals(originalClinical.getId(), resultClinical.getId());
        assertEquals(originalClinical.getPatientId(), resultClinical.getPatientId());
        assertEquals(originalClinical.getComponentName(), resultClinical.getComponentName());
        assertEquals(originalClinical.getComponentValue(), resultClinical.getComponentValue());
        assertEquals(originalClinical.getMeasuredDateTime(), resultClinical.getMeasuredDateTime());
    }
}
