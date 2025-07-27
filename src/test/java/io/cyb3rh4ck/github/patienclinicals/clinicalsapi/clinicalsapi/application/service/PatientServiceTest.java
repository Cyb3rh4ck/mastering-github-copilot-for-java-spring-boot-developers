package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.port.PatientRepository;
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
class PatientServiceTest {
    
    @Mock
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientService patientService;
    
    private Patient patient;
    
    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
    }
    
    @Test
    void testCreatePatient_Success() {
        // Given
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Smith");
        newPatient.setAge(25);
        
        when(patientRepository.save(newPatient)).thenReturn(newPatient);
        
        // When
        Patient result = patientService.createPatient(newPatient);
        
        // Then
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(25, result.getAge());
        verify(patientRepository).save(newPatient);
    }
    
    @Test
    void testCreatePatient_WithNullFirstName() {
        // Given
        Patient newPatient = new Patient();
        newPatient.setFirstName(null);
        newPatient.setLastName("Smith");
        newPatient.setAge(25);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> patientService.createPatient(newPatient));
        assertEquals("First name and last name are required", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }
    
    @Test
    void testCreatePatient_WithNullLastName() {
        // Given
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName(null);
        newPatient.setAge(25);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> patientService.createPatient(newPatient));
        assertEquals("First name and last name are required", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }
    
    @Test
    void testGetPatientById_Found() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        
        // When
        Optional<Patient> result = patientService.getPatientById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(patient.getId(), result.get().getId());
        assertEquals(patient.getFirstName(), result.get().getFirstName());
        verify(patientRepository).findById(1L);
    }
    
    @Test
    void testGetPatientById_NotFound() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<Patient> result = patientService.getPatientById(999L);
        
        // Then
        assertFalse(result.isPresent());
        verify(patientRepository).findById(999L);
    }
    
    @Test
    void testGetAllPatients() {
        // Given
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setAge(25);
        
        List<Patient> patients = Arrays.asList(patient, patient2);
        when(patientRepository.findAll()).thenReturn(patients);
        
        // When
        List<Patient> result = patientService.getAllPatients();
        
        // Then
        assertEquals(2, result.size());
        assertEquals(patient.getFirstName(), result.get(0).getFirstName());
        assertEquals(patient2.getFirstName(), result.get(1).getFirstName());
        verify(patientRepository).findAll();
    }
    
    @Test
    void testGetPatientsByLastName() {
        // Given
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findByLastName("Doe")).thenReturn(patients);
        
        // When
        List<Patient> result = patientService.getPatientsByLastName("Doe");
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        verify(patientRepository).findByLastName("Doe");
    }
    
    @Test
    void testUpdatePatient_Success() {
        // Given
        Patient updatedData = new Patient();
        updatedData.setFirstName("Jane");
        updatedData.setLastName("Smith");
        updatedData.setAge(35);
        
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        
        // When
        Patient result = patientService.updatePatient(1L, updatedData);
        
        // Then
        assertNotNull(result);
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
        
        // Verify that the patient object was updated
        assertEquals("Jane", patient.getFirstName());
        assertEquals("Smith", patient.getLastName());
        assertEquals(35, patient.getAge());
    }
    
    @Test
    void testUpdatePatient_NotFound() {
        // Given
        Patient updatedData = new Patient();
        updatedData.setFirstName("Jane");
        updatedData.setLastName("Smith");
        updatedData.setAge(35);
        
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> patientService.updatePatient(999L, updatedData));
        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository).findById(999L);
        verify(patientRepository, never()).save(any());
    }
    
    @Test
    void testDeletePatient() {
        // When
        patientService.deletePatient(1L);
        
        // Then
        verify(patientRepository).deleteById(1L);
    }
}
