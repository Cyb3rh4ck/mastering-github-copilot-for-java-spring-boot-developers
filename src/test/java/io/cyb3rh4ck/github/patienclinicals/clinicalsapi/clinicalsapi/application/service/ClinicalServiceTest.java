package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.ClinicalRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalServiceTest {

    @Mock
    private ClinicalRepository clinicalRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private ClinicalService clinicalService;

    private Clinical clinical;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);

        clinical = new Clinical();
        clinical.setId(1L);
        clinical.setPatientId(1L);
        clinical.setComponentName("Blood Pressure");
        clinical.setComponentValue("120/80");
        clinical.setMeasuredDateTime(LocalDateTime.now());
    }

    @Test
    void testCreateClinical_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(clinicalRepository.save(clinical)).thenReturn(clinical);

        // When
        Clinical result = clinicalService.createClinical(clinical);

        // Then
        assertNotNull(result);
        assertEquals(clinical.getId(), result.getId());
        assertEquals(clinical.getPatientId(), result.getPatientId());
        assertEquals(clinical.getComponentName(), result.getComponentName());
        assertEquals(clinical.getComponentValue(), result.getComponentValue());
        
        verify(patientRepository).findById(1L);
        verify(clinicalRepository).save(clinical);
    }

    @Test
    void testCreateClinical_PatientNotFound() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clinicalService.createClinical(clinical)
        );
        
        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(clinicalRepository, never()).save(any(Clinical.class));
    }

    @Test
    void testGetClinicalById_Found() {
        // Given
        when(clinicalRepository.findById(1L)).thenReturn(Optional.of(clinical));

        // When
        Optional<Clinical> result = clinicalService.getClinicalById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(clinical.getId(), result.get().getId());
        assertEquals(clinical.getComponentName(), result.get().getComponentName());
        
        verify(clinicalRepository).findById(1L);
    }

    @Test
    void testGetClinicalById_NotFound() {
        // Given
        when(clinicalRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Clinical> result = clinicalService.getClinicalById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(clinicalRepository).findById(1L);
    }

    @Test
    void testGetAllClinicals() {
        // Given
        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(2L);
        clinical2.setComponentName("Heart Rate");
        clinical2.setComponentValue("72 bpm");
        clinical2.setMeasuredDateTime(LocalDateTime.now());

        List<Clinical> clinicals = Arrays.asList(clinical, clinical2);
        when(clinicalRepository.findAll()).thenReturn(clinicals);

        // When
        List<Clinical> result = clinicalService.getAllClinicals();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(clinical.getId(), result.get(0).getId());
        assertEquals(clinical2.getId(), result.get(1).getId());
        
        verify(clinicalRepository).findAll();
    }

    @Test
    void testGetClinicalsByPatientId() {
        // Given
        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(1L);
        clinical2.setComponentName("Heart Rate");
        clinical2.setComponentValue("72 bpm");
        clinical2.setMeasuredDateTime(LocalDateTime.now());

        List<Clinical> clinicals = Arrays.asList(clinical, clinical2);
        when(clinicalRepository.findByPatientId(1L)).thenReturn(clinicals);

        // When
        List<Clinical> result = clinicalService.getClinicalsByPatientId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPatientId());
        assertEquals(1L, result.get(1).getPatientId());
        
        verify(clinicalRepository).findByPatientId(1L);
    }

    @Test
    void testUpdateClinical_Success() {
        // Given
        Clinical updatedClinical = new Clinical();
        updatedClinical.setComponentName("Updated Blood Pressure");
        updatedClinical.setComponentValue("130/85");
        updatedClinical.setMeasuredDateTime(LocalDateTime.now().plusDays(1));

        when(clinicalRepository.findById(1L)).thenReturn(Optional.of(clinical));
        when(clinicalRepository.save(any(Clinical.class))).thenReturn(clinical);

        // When
        Clinical result = clinicalService.updateClinical(1L, updatedClinical);

        // Then
        assertNotNull(result);
        verify(clinicalRepository).findById(1L);
        verify(clinicalRepository).save(clinical);
    }

    @Test
    void testUpdateClinical_NotFound() {
        // Given
        Clinical updatedClinical = new Clinical();
        updatedClinical.setComponentName("Updated Blood Pressure");
        updatedClinical.setComponentValue("130/85");

        when(clinicalRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clinicalService.updateClinical(1L, updatedClinical)
        );
        
        assertEquals("Clinical not found", exception.getMessage());
        verify(clinicalRepository).findById(1L);
        verify(clinicalRepository, never()).save(any(Clinical.class));
    }

    @Test
    void testDeleteClinical() {
        // Given
        doNothing().when(clinicalRepository).deleteById(1L);

        // When
        clinicalService.deleteClinical(1L);

        // Then
        verify(clinicalRepository).deleteById(1L);
    }
}
