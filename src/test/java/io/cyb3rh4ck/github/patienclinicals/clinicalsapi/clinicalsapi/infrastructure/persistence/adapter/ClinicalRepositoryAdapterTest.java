package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.adapter;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.ClinicalEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper.ClinicalMapper;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaClinicalRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaPatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalRepositoryAdapterTest {

    @Mock
    private JpaClinicalRepository jpaClinicalRepository;

    @Mock
    private JpaPatientRepository jpaPatientRepository;

    @Mock
    private ClinicalMapper clinicalMapper;

    @InjectMocks
    private ClinicalRepositoryAdapter clinicalRepositoryAdapter;

    private Clinical clinical;
    private ClinicalEntity clinicalEntity;
    private PatientEntity patientEntity;

    @BeforeEach
    void setUp() {
        clinical = new Clinical();
        clinical.setId(1L);
        clinical.setPatientId(1L);
        clinical.setComponentName("Blood Pressure");
        clinical.setComponentValue("120/80");
        clinical.setMeasuredDateTime(LocalDateTime.now());

        clinicalEntity = new ClinicalEntity();
        clinicalEntity.setId(1L);
        clinicalEntity.setComponentName("Blood Pressure");
        clinicalEntity.setComponentValue("120/80");
        clinicalEntity.setMeasuredDateTime(Timestamp.valueOf(LocalDateTime.now()));

        patientEntity = new PatientEntity();
        patientEntity.setId(1L);
        patientEntity.setFirstName("John");
        patientEntity.setLastName("Doe");
        patientEntity.setAge(30);
    }

    @Test
    void testSave_WithPatientId() {
        // Given
        when(clinicalMapper.toEntity(clinical)).thenReturn(clinicalEntity);
        when(jpaPatientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(jpaClinicalRepository.save(clinicalEntity)).thenReturn(clinicalEntity);
        when(clinicalMapper.toDomain(clinicalEntity)).thenReturn(clinical);

        // When
        Clinical result = clinicalRepositoryAdapter.save(clinical);

        // Then
        assertNotNull(result);
        assertEquals(clinical.getId(), result.getId());
        assertEquals(clinical.getPatientId(), result.getPatientId());
        assertEquals(clinical.getComponentName(), result.getComponentName());

        verify(clinicalMapper).toEntity(clinical);
        verify(jpaPatientRepository).findById(1L);
        verify(jpaClinicalRepository).save(clinicalEntity);
        verify(clinicalMapper).toDomain(clinicalEntity);
    }

    @Test
    void testSave_PatientNotFound() {
        // Given
        when(clinicalMapper.toEntity(clinical)).thenReturn(clinicalEntity);
        when(jpaPatientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clinicalRepositoryAdapter.save(clinical)
        );

        assertEquals("Patient not found", exception.getMessage());
        verify(clinicalMapper).toEntity(clinical);
        verify(jpaPatientRepository).findById(1L);
        verify(jpaClinicalRepository, never()).save(any());
        verify(clinicalMapper, never()).toDomain(any());
    }

    @Test
    void testSave_WithoutPatientId() {
        // Given
        clinical.setPatientId(null);
        when(clinicalMapper.toEntity(clinical)).thenReturn(clinicalEntity);
        when(jpaClinicalRepository.save(clinicalEntity)).thenReturn(clinicalEntity);
        when(clinicalMapper.toDomain(clinicalEntity)).thenReturn(clinical);

        // When
        Clinical result = clinicalRepositoryAdapter.save(clinical);

        // Then
        assertNotNull(result);
        assertEquals(clinical.getId(), result.getId());
        assertEquals(clinical.getComponentName(), result.getComponentName());

        verify(clinicalMapper).toEntity(clinical);
        verify(jpaPatientRepository, never()).findById(any());
        verify(jpaClinicalRepository).save(clinicalEntity);
        verify(clinicalMapper).toDomain(clinicalEntity);
    }

    @Test
    void testFindById_Found() {
        // Given
        when(jpaClinicalRepository.findById(1L)).thenReturn(Optional.of(clinicalEntity));
        when(clinicalMapper.toDomain(clinicalEntity)).thenReturn(clinical);

        // When
        Optional<Clinical> result = clinicalRepositoryAdapter.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(clinical.getId(), result.get().getId());
        assertEquals(clinical.getComponentName(), result.get().getComponentName());

        verify(jpaClinicalRepository).findById(1L);
        verify(clinicalMapper).toDomain(clinicalEntity);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(jpaClinicalRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Clinical> result = clinicalRepositoryAdapter.findById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(jpaClinicalRepository).findById(1L);
        verify(clinicalMapper, never()).toDomain(any());
    }

    @Test
    void testFindAll() {
        // Given
        ClinicalEntity clinicalEntity2 = new ClinicalEntity();
        clinicalEntity2.setId(2L);
        clinicalEntity2.setComponentName("Heart Rate");
        clinicalEntity2.setComponentValue("72 bpm");

        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(2L);
        clinical2.setComponentName("Heart Rate");
        clinical2.setComponentValue("72 bpm");

        List<ClinicalEntity> entities = Arrays.asList(clinicalEntity, clinicalEntity2);
        when(jpaClinicalRepository.findAll()).thenReturn(entities);
        when(clinicalMapper.toDomain(clinicalEntity)).thenReturn(clinical);
        when(clinicalMapper.toDomain(clinicalEntity2)).thenReturn(clinical2);

        // When
        List<Clinical> result = clinicalRepositoryAdapter.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(clinical.getId(), result.get(0).getId());
        assertEquals(clinical2.getId(), result.get(1).getId());

        verify(jpaClinicalRepository).findAll();
        verify(clinicalMapper, times(2)).toDomain(any(ClinicalEntity.class));
    }

    @Test
    void testFindByPatientId() {
        // Given
        ClinicalEntity clinicalEntity2 = new ClinicalEntity();
        clinicalEntity2.setId(2L);
        clinicalEntity2.setComponentName("Heart Rate");
        clinicalEntity2.setComponentValue("72 bpm");

        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(1L);
        clinical2.setComponentName("Heart Rate");
        clinical2.setComponentValue("72 bpm");

        List<ClinicalEntity> entities = Arrays.asList(clinicalEntity, clinicalEntity2);
        when(jpaClinicalRepository.findByPatientId(1L)).thenReturn(entities);
        when(clinicalMapper.toDomain(clinicalEntity)).thenReturn(clinical);
        when(clinicalMapper.toDomain(clinicalEntity2)).thenReturn(clinical2);

        // When
        List<Clinical> result = clinicalRepositoryAdapter.findByPatientId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPatientId());
        assertEquals(1L, result.get(1).getPatientId());

        verify(jpaClinicalRepository).findByPatientId(1L);
        verify(clinicalMapper, times(2)).toDomain(any(ClinicalEntity.class));
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(jpaClinicalRepository).deleteById(1L);

        // When
        clinicalRepositoryAdapter.deleteById(1L);

        // Then
        verify(jpaClinicalRepository).deleteById(1L);
    }
}
