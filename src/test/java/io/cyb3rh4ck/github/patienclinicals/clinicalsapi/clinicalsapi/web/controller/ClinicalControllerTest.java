package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.web.controller;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service.ClinicalService;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClinicalControllerTest {

    @Mock
    private ClinicalService clinicalService;

    @InjectMocks
    private ClinicalController clinicalController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Clinical clinical;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clinicalController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDateTime serialization
        
        clinical = new Clinical();
        clinical.setId(1L);
        clinical.setPatientId(1L);
        clinical.setComponentName("Blood Pressure");
        clinical.setComponentValue("120/80");
        clinical.setMeasuredDateTime(LocalDateTime.of(2025, 1, 15, 10, 30));
    }

    @Test
    void testCreateClinical_Success() throws Exception {
        // Given
        when(clinicalService.createClinical(any(Clinical.class))).thenReturn(clinical);

        // When & Then
        mockMvc.perform(post("/clinicals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinical)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.componentName").value("Blood Pressure"))
                .andExpect(jsonPath("$.componentValue").value("120/80"));

        verify(clinicalService).createClinical(any(Clinical.class));
    }

    @Test
    void testCreateClinical_BadRequest() throws Exception {
        // Given
        when(clinicalService.createClinical(any(Clinical.class)))
                .thenThrow(new IllegalArgumentException("Patient not found"));

        // When & Then
        mockMvc.perform(post("/clinicals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinical)))
                .andExpect(status().isBadRequest());

        verify(clinicalService).createClinical(any(Clinical.class));
    }

    @Test
    void testGetAllClinicals() throws Exception {
        // Given
        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(1L);
        clinical2.setComponentName("Heart Rate");
        clinical2.setComponentValue("72");
        clinical2.setMeasuredDateTime(LocalDateTime.of(2025, 1, 15, 11, 30));

        List<Clinical> clinicals = Arrays.asList(clinical, clinical2);
        when(clinicalService.getAllClinicals()).thenReturn(clinicals);

        // When & Then
        mockMvc.perform(get("/clinicals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].componentName").value("Blood Pressure"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].componentName").value("Heart Rate"));

        verify(clinicalService).getAllClinicals();
    }

    @Test
    void testGetClinicalById_Found() throws Exception {
        // Given
        when(clinicalService.getClinicalById(1L)).thenReturn(Optional.of(clinical));

        // When & Then
        mockMvc.perform(get("/clinicals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.componentName").value("Blood Pressure"))
                .andExpect(jsonPath("$.componentValue").value("120/80"));

        verify(clinicalService).getClinicalById(1L);
    }

    @Test
    void testGetClinicalById_NotFound() throws Exception {
        // Given
        when(clinicalService.getClinicalById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/clinicals/1"))
                .andExpect(status().isNotFound());

        verify(clinicalService).getClinicalById(1L);
    }

    @Test
    void testGetClinicalsByPatientId() throws Exception {
        // Given
        Clinical clinical2 = new Clinical();
        clinical2.setId(2L);
        clinical2.setPatientId(1L);
        clinical2.setComponentName("Temperature");
        clinical2.setComponentValue("98.6");
        clinical2.setMeasuredDateTime(LocalDateTime.of(2025, 1, 15, 12, 0));

        List<Clinical> clinicals = Arrays.asList(clinical, clinical2);
        when(clinicalService.getClinicalsByPatientId(1L)).thenReturn(clinicals);

        // When & Then
        mockMvc.perform(get("/clinicals/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[1].patientId").value(1));

        verify(clinicalService).getClinicalsByPatientId(1L);
    }

    @Test
    void testUpdateClinical_Success() throws Exception {
        // Given
        Clinical updatedClinical = new Clinical();
        updatedClinical.setId(1L);
        updatedClinical.setPatientId(1L);
        updatedClinical.setComponentName("Blood Pressure");
        updatedClinical.setComponentValue("130/85");
        updatedClinical.setMeasuredDateTime(LocalDateTime.of(2025, 1, 15, 14, 0));

        when(clinicalService.updateClinical(eq(1L), any(Clinical.class))).thenReturn(updatedClinical);

        // When & Then
        mockMvc.perform(put("/clinicals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedClinical)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.componentValue").value("130/85"));

        verify(clinicalService).updateClinical(eq(1L), any(Clinical.class));
    }

    @Test
    void testUpdateClinical_NotFound() throws Exception {
        // Given
        when(clinicalService.updateClinical(eq(1L), any(Clinical.class)))
                .thenThrow(new RuntimeException("Clinical not found"));

        // When & Then
        mockMvc.perform(put("/clinicals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinical)))
                .andExpect(status().isNotFound());

        verify(clinicalService).updateClinical(eq(1L), any(Clinical.class));
    }

    @Test
    void testDeleteClinical() throws Exception {
        // Given
        doNothing().when(clinicalService).deleteClinical(1L);

        // When & Then
        mockMvc.perform(delete("/clinicals/1"))
                .andExpect(status().isNoContent());

        verify(clinicalService).deleteClinical(1L);
    }
}
