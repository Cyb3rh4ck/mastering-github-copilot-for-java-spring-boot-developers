package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.web.controller;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service.PatientService;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {
    
    @Mock
    private PatientService patientService;
    
    @InjectMocks
    private PatientController patientController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Patient patient;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
        objectMapper = new ObjectMapper();
        
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
    }
    
    @Test
    void testCreatePatient_Success() throws Exception {
        // Given
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Smith");
        newPatient.setAge(25);
        
        Patient savedPatient = new Patient();
        savedPatient.setId(2L);
        savedPatient.setFirstName("Jane");
        savedPatient.setLastName("Smith");
        savedPatient.setAge(25);
        
        when(patientService.createPatient(any(Patient.class))).thenReturn(savedPatient);
        
        // When & Then
        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.age").value(25));
        
        verify(patientService).createPatient(any(Patient.class));
    }
    
    @Test
    void testCreatePatient_ValidationError() throws Exception {
        // Given
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Jane");
        // Missing lastName
        
        when(patientService.createPatient(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("First name and last name are required"));
        
        // When & Then
        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());
        
        verify(patientService).createPatient(any(Patient.class));
    }
    
    @Test
    void testGetAllPatients() throws Exception {
        // Given
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setAge(25);
        
        List<Patient> patients = Arrays.asList(patient, patient2);
        when(patientService.getAllPatients()).thenReturn(patients);
        
        // When & Then
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
        
        verify(patientService).getAllPatients();
    }
    
    @Test
    void testGetPatientById_Found() throws Exception {
        // Given
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
        
        // When & Then
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));
        
        verify(patientService).getPatientById(1L);
    }
    
    @Test
    void testGetPatientById_NotFound() throws Exception {
        // Given
        when(patientService.getPatientById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/patients/999"))
                .andExpect(status().isNotFound());
        
        verify(patientService).getPatientById(999L);
    }
    
    @Test
    void testGetPatientsByLastName() throws Exception {
        // Given
        List<Patient> patients = Arrays.asList(patient);
        when(patientService.getPatientsByLastName("Doe")).thenReturn(patients);
        
        // When & Then
        mockMvc.perform(get("/patients/lastname/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
        
        verify(patientService).getPatientsByLastName("Doe");
    }
    
    @Test
    void testUpdatePatient_Success() throws Exception {
        // Given
        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstName("Johnny");
        updatedPatient.setLastName("Doe");
        updatedPatient.setAge(31);
        
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updatedPatient);
        
        // When & Then
        mockMvc.perform(put("/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.age").value(31));
        
        verify(patientService).updatePatient(eq(1L), any(Patient.class));
    }
    
    @Test
    void testUpdatePatient_NotFound() throws Exception {
        // Given
        Patient updatedData = new Patient();
        updatedData.setFirstName("Johnny");
        updatedData.setLastName("Doe");
        updatedData.setAge(31);
        
        when(patientService.updatePatient(eq(999L), any(Patient.class)))
                .thenThrow(new RuntimeException("Patient not found"));
        
        // When & Then
        mockMvc.perform(put("/patients/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isNotFound());
        
        verify(patientService).updatePatient(eq(999L), any(Patient.class));
    }
    
    @Test
    void testDeletePatient() throws Exception {
        // Given
        doNothing().when(patientService).deletePatient(1L);
        
        // When & Then
        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNoContent());
        
        verify(patientService).deletePatient(1L);
    }
    
    @Test
    void testCreatePatient_WithCompleteData() throws Exception {
        // Given
        Patient completePatient = new Patient();
        completePatient.setFirstName("Complete");
        completePatient.setLastName("Patient");
        completePatient.setAge(40);
        
        Patient savedPatient = new Patient();
        savedPatient.setId(3L);
        savedPatient.setFirstName("Complete");
        savedPatient.setLastName("Patient");
        savedPatient.setAge(40);
        
        when(patientService.createPatient(any(Patient.class))).thenReturn(savedPatient);
        
        // When & Then
        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completePatient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Complete"))
                .andExpect(jsonPath("$.lastName").value("Patient"))
                .andExpect(jsonPath("$.age").value(40));
        
        verify(patientService).createPatient(any(Patient.class));
    }
}
