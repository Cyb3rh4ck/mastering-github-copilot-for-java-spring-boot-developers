package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.adapter;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper.PatientMapper;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaPatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryAdapterTest {
    
    @Mock
    private JpaPatientRepository jpaPatientRepository;
    
    @Mock
    private PatientMapper patientMapper;
    
    @InjectMocks
    private PatientRepositoryAdapter patientRepositoryAdapter;
    
    private Patient patient;
    private PatientEntity patientEntity;
    
    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
        
        patientEntity = new PatientEntity();
        patientEntity.setId(1L);
        patientEntity.setFirstName("John");
        patientEntity.setLastName("Doe");
        patientEntity.setAge(30);
    }
    
    @Test
    void testSave() {
        // Given
        when(patientMapper.toEntity(patient)).thenReturn(patientEntity);
        when(jpaPatientRepository.save(patientEntity)).thenReturn(patientEntity);
        when(patientMapper.toDomain(patientEntity)).thenReturn(patient);
        
        // When
        Patient result = patientRepositoryAdapter.save(patient);
        
        // Then
        assertNotNull(result);
        assertEquals(patient.getId(), result.getId());
        assertEquals(patient.getFirstName(), result.getFirstName());
        assertEquals(patient.getLastName(), result.getLastName());
        assertEquals(patient.getAge(), result.getAge());
        verify(patientMapper).toEntity(patient);
        verify(jpaPatientRepository).save(patientEntity);
        verify(patientMapper).toDomain(patientEntity);
    }
    
    @Test
    void testFindById_Found() {
        // Given
        when(jpaPatientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(patientMapper.toDomain(patientEntity)).thenReturn(patient);
        
        // When
        Optional<Patient> result = patientRepositoryAdapter.findById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(patient.getId(), result.get().getId());
        assertEquals(patient.getFirstName(), result.get().getFirstName());
        assertEquals(patient.getLastName(), result.get().getLastName());
        assertEquals(patient.getAge(), result.get().getAge());
        verify(jpaPatientRepository).findById(1L);
        verify(patientMapper).toDomain(patientEntity);
    }
    
    @Test
    void testFindById_NotFound() {
        // Given
        when(jpaPatientRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<Patient> result = patientRepositoryAdapter.findById(999L);
        
        // Then
        assertFalse(result.isPresent());
        verify(jpaPatientRepository).findById(999L);
        verify(patientMapper, never()).toDomain(any());
    }
    
    @Test
    void testFindAll() {
        // Given
        PatientEntity patientEntity2 = new PatientEntity();
        patientEntity2.setId(2L);
        patientEntity2.setFirstName("Jane");
        patientEntity2.setLastName("Smith");
        patientEntity2.setAge(25);
        
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setAge(25);
        
        List<PatientEntity> entities = Arrays.asList(patientEntity, patientEntity2);
        when(jpaPatientRepository.findAll()).thenReturn(entities);
        when(patientMapper.toDomain(patientEntity)).thenReturn(patient);
        when(patientMapper.toDomain(patientEntity2)).thenReturn(patient2);
        
        // When
        List<Patient> result = patientRepositoryAdapter.findAll();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(jpaPatientRepository).findAll();
        verify(patientMapper).toDomain(patientEntity);
        verify(patientMapper).toDomain(patientEntity2);
    }
    
    @Test
    void testDeleteById() {
        // Given
        doNothing().when(jpaPatientRepository).deleteById(1L);
        
        // When
        patientRepositoryAdapter.deleteById(1L);
        
        // Then
        verify(jpaPatientRepository).deleteById(1L);
    }
    
    @Test
    void testFindByLastName_Found() {
        // Given
        String lastName = "Doe";
        List<PatientEntity> entities = Arrays.asList(patientEntity);
        when(jpaPatientRepository.findByLastName(lastName)).thenReturn(entities);
        when(patientMapper.toDomain(patientEntity)).thenReturn(patient);
        
        // When
        List<Patient> result = patientRepositoryAdapter.findByLastName(lastName);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(lastName, result.get(0).getLastName());
        verify(jpaPatientRepository).findByLastName(lastName);
        verify(patientMapper).toDomain(patientEntity);
    }
    
    @Test
    void testFindByLastName_NotFound() {
        // Given
        String lastName = "NonExistent";
        when(jpaPatientRepository.findByLastName(lastName)).thenReturn(Arrays.asList());
        
        // When
        List<Patient> result = patientRepositoryAdapter.findByLastName(lastName);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaPatientRepository).findByLastName(lastName);
        verify(patientMapper, never()).toDomain(any());
    }
    
    @Test
    void testFindAll_EmptyList() {
        // Given
        when(jpaPatientRepository.findAll()).thenReturn(Arrays.asList());
        
        // When
        List<Patient> result = patientRepositoryAdapter.findAll();
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaPatientRepository).findAll();
        verify(patientMapper, never()).toDomain(any());
    }
}
