package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "clinicaldata")
public class ClinicalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private PatientEntity patient;

    @Column(name = "component_name", nullable = false)
    private String componentName;

    @Column(name = "component_value", nullable = false)
    private String componentValue;

    @CreationTimestamp
    @Column(name = "measured_date_time", nullable = false)
    private Timestamp measuredDateTime;

    // Constructors
    public ClinicalEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PatientEntity getPatient() { return patient; }
    public void setPatient(PatientEntity patient) { this.patient = patient; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public String getComponentValue() { return componentValue; }
    public void setComponentValue(String componentValue) { this.componentValue = componentValue; }

    public Timestamp getMeasuredDateTime() { return measuredDateTime; }
    public void setMeasuredDateTime(Timestamp measuredDateTime) { this.measuredDateTime = measuredDateTime; }
}
