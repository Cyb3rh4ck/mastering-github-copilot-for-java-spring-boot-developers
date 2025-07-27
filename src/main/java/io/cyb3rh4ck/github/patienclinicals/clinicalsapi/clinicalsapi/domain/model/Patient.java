package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model;

import java.util.List;

public class Patient {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Clinical> clinicals;

    // Constructors
    public Patient() {}

    public Patient(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public List<Clinical> getClinicals() { return clinicals; }
    public void setClinicals(List<Clinical> clinicals) { this.clinicals = clinicals; }
}

