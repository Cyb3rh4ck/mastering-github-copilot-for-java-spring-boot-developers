package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model;

import java.time.LocalDateTime;

public class Clinical {
    private Long id;
    private Long patientId;
    private String componentName;
    private String componentValue;
    private LocalDateTime measuredDateTime;

    // Constructors
    public Clinical() {}

    public Clinical(Long patientId, String componentName, String componentValue, LocalDateTime measuredDateTime) {
        this.patientId = patientId;
        this.componentName = componentName;
        this.componentValue = componentValue;
        this.measuredDateTime = measuredDateTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public String getComponentValue() { return componentValue; }
    public void setComponentValue(String componentValue) { this.componentValue = componentValue; }

    public LocalDateTime getMeasuredDateTime() { return measuredDateTime; }
    public void setMeasuredDateTime(LocalDateTime measuredDateTime) { this.measuredDateTime = measuredDateTime; }
}