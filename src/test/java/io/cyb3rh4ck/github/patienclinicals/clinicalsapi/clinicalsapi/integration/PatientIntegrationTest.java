package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.integration;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Patient;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.PatientEntity;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.repository.JpaPatientRepository;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.application.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PatientIntegrationTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JpaPatientRepository jpaPatientRepository;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        // Clean up database
        jpaPatientRepository.deleteAll();

        // Set up test data
        testPatient = new Patient();
        testPatient.setFirstName("Integration");
        testPatient.setLastName("Test");
        testPatient.setAge(35);
    }

    @Test
    void testCreatePatient_Integration() {
        // When
        Patient savedPatient = patientService.createPatient(testPatient);

        // Then
        assertNotNull(savedPatient);
        assertNotNull(savedPatient.getId());
        assertEquals("Integration", savedPatient.getFirstName());
        assertEquals("Test", savedPatient.getLastName());
        assertEquals(35, savedPatient.getAge());

        // Verify in database
        Optional<PatientEntity> entityInDb = jpaPatientRepository.findById(savedPatient.getId());
        assertTrue(entityInDb.isPresent());
        assertEquals("Integration", entityInDb.get().getFirstName());
        assertEquals("Test", entityInDb.get().getLastName());
        assertEquals(35, entityInDb.get().getAge());
    }

    @Test
    void testFindPatientById_Integration() {
        // Given
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setFirstName("Find");
        patientEntity.setLastName("Test");
        patientEntity.setAge(28);
        PatientEntity savedEntity = jpaPatientRepository.save(patientEntity);

        // When
        Optional<Patient> foundPatient = patientService.getPatientById(savedEntity.getId());

        // Then
        assertTrue(foundPatient.isPresent());
        assertEquals("Find", foundPatient.get().getFirstName());
        assertEquals("Test", foundPatient.get().getLastName());
        assertEquals(28, foundPatient.get().getAge());
    }

    @Test
    void testUpdatePatient_Integration() {
        // Given
        PatientEntity originalEntity = new PatientEntity();
        originalEntity.setFirstName("Original");
        originalEntity.setLastName("Patient");
        originalEntity.setAge(25);
        PatientEntity savedEntity = jpaPatientRepository.save(originalEntity);

        // When
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Updated");
        updatedPatient.setLastName("Patient");
        updatedPatient.setAge(26);

        Patient result = patientService.updatePatient(savedEntity.getId(), updatedPatient);

        // Then
        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("Patient", result.getLastName());
        assertEquals(26, result.getAge());

        // Verify in database
        Optional<PatientEntity> updatedEntity = jpaPatientRepository.findById(savedEntity.getId());
        assertTrue(updatedEntity.isPresent());
        assertEquals("Updated", updatedEntity.get().getFirstName());
        assertEquals(26, updatedEntity.get().getAge());
    }

    @Test
    void testDeletePatient_Integration() {
        // Given
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setFirstName("Delete");
        patientEntity.setLastName("Test");
        patientEntity.setAge(30);
        PatientEntity savedEntity = jpaPatientRepository.save(patientEntity);

        // When
        patientService.deletePatient(savedEntity.getId());

        // Then
        Optional<PatientEntity> deletedEntity = jpaPatientRepository.findById(savedEntity.getId());
        assertFalse(deletedEntity.isPresent());
    }

    @Test
    void testFindAllPatients_Integration() {
        // Given
        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("Patient");
        patient1.setLastName("One");
        patient1.setAge(25);

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Patient");
        patient2.setLastName("Two");
        patient2.setAge(30);

        jpaPatientRepository.save(patient1);
        jpaPatientRepository.save(patient2);

        // When
        List<Patient> allPatients = patientService.getAllPatients();

        // Then
        assertNotNull(allPatients);
        assertEquals(2, allPatients.size());

        // Verify both patients are present
        assertTrue(allPatients.stream().anyMatch(p -> "One".equals(p.getLastName())));
        assertTrue(allPatients.stream().anyMatch(p -> "Two".equals(p.getLastName())));
    }

    @Test
    void testFindByLastName_Integration() {
        // Given
        PatientEntity existingPatient = new PatientEntity();
        existingPatient.setFirstName("Existing");
        existingPatient.setLastName("SearchTest");
        existingPatient.setAge(40);
        jpaPatientRepository.save(existingPatient);

        // When
        List<Patient> foundPatients = patientService.getPatientsByLastName("SearchTest");

        // Then
        assertNotNull(foundPatients);
        assertEquals(1, foundPatients.size());
        assertEquals("SearchTest", foundPatients.get(0).getLastName());
        assertEquals("Existing", foundPatients.get(0).getFirstName());
    }

    @Test
    void testCreatePatient_ValidationError_Integration() {
        // Given
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Valid");
        // Missing lastName

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatient(invalidPatient));
        assertEquals("First name and last name are required", exception.getMessage());

        // Verify no patient was saved
        List<PatientEntity> allEntities = jpaPatientRepository.findAll();
        assertTrue(allEntities.isEmpty());
    }

    @Test
    void testFindByLastName_EmptyResult_Integration() {
        // Given - No patients with the searched last name

        // When
        List<Patient> foundPatients = patientService.getPatientsByLastName("NonExistent");

        // Then
        assertNotNull(foundPatients);
        assertTrue(foundPatients.isEmpty());
    }

    @Test
    void testFindByLastName_MultipleResults_Integration() {
        // Given
        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("John");
        patient1.setLastName("Smith");
        patient1.setAge(25);

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setAge(30);

        PatientEntity patient3 = new PatientEntity();
        patient3.setFirstName("Bob");
        patient3.setLastName("Johnson");
        patient3.setAge(35);

        jpaPatientRepository.save(patient1);
        jpaPatientRepository.save(patient2);
        jpaPatientRepository.save(patient3);

        // When
        List<Patient> smithPatients = patientService.getPatientsByLastName("Smith");

        // Then
        assertNotNull(smithPatients);
        assertEquals(2, smithPatients.size());
        assertTrue(smithPatients.stream().allMatch(p -> "Smith".equals(p.getLastName())));
        assertTrue(smithPatients.stream().anyMatch(p -> "John".equals(p.getFirstName())));
        assertTrue(smithPatients.stream().anyMatch(p -> "Jane".equals(p.getFirstName())));
    }

    @Test
    void testUpdatePatient_NotFound_Integration() {
        // Given
        Long nonExistentId = 999L;
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Updated");
        updatedPatient.setLastName("Patient");
        updatedPatient.setAge(26);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> patientService.updatePatient(nonExistentId, updatedPatient));
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void testCreatePatient_MissingFirstName_Integration() {
        // Given
        Patient invalidPatient = new Patient();
        invalidPatient.setLastName("ValidLastName");
        invalidPatient.setAge(25);
        // Missing firstName

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatient(invalidPatient));
        assertEquals("First name and last name are required", exception.getMessage());

        // Verify no patient was saved
        List<PatientEntity> allEntities = jpaPatientRepository.findAll();
        assertTrue(allEntities.isEmpty());
    }

    @Test
    void testCreatePatient_BothNamesEmpty_Integration() {
        // Given
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("");
        invalidPatient.setLastName("");
        invalidPatient.setAge(25);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatient(invalidPatient));
        assertEquals("First name and last name are required", exception.getMessage());

        // Verify no patient was saved
        List<PatientEntity> allEntities = jpaPatientRepository.findAll();
        assertTrue(allEntities.isEmpty());
    }

    @Test
    void testGetPatientById_NotFound_Integration() {
        // Given
        Long nonExistentId = 999L;

        // When
        Optional<Patient> foundPatient = patientService.getPatientById(nonExistentId);

        // Then
        assertFalse(foundPatient.isPresent());
    }

    @Test
    void testEndToEndWorkflow_Integration() {
        // Create a patient
        Patient newPatient = new Patient();
        newPatient.setFirstName("EndToEnd");
        newPatient.setLastName("Test");
        newPatient.setAge(45);

        Patient createdPatient = patientService.createPatient(newPatient);
        assertNotNull(createdPatient.getId());

        // Find the patient by ID
        Optional<Patient> foundPatient = patientService.getPatientById(createdPatient.getId());
        assertTrue(foundPatient.isPresent());
        assertEquals("EndToEnd", foundPatient.get().getFirstName());

        // Update the patient
        Patient updateData = new Patient();
        updateData.setFirstName("Updated");
        updateData.setLastName("Test");
        updateData.setAge(46);

        Patient updatedPatient = patientService.updatePatient(createdPatient.getId(), updateData);
        assertEquals("Updated", updatedPatient.getFirstName());
        assertEquals(46, updatedPatient.getAge());

        // Find by last name
        List<Patient> patientsByLastName = patientService.getPatientsByLastName("Test");
        assertEquals(1, patientsByLastName.size());
        assertEquals("Updated", patientsByLastName.get(0).getFirstName());

        // Delete the patient
        patientService.deletePatient(createdPatient.getId());

        // Verify deletion
        Optional<Patient> deletedPatient = patientService.getPatientById(createdPatient.getId());
        assertFalse(deletedPatient.isPresent());
    }
}