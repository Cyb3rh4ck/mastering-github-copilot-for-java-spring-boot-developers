package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "patient")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClinicalEntity> clinicals;

    // Constructors
    public PatientEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public List<ClinicalEntity> getClinicals() { return clinicals; }
    public void setClinicals(List<ClinicalEntity> clinicals) { this.clinicals = clinicals; }
}