package com.course.REST.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "student")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty(required = true)
    @Size(min = 4, max = 15, message = "El nombre debe tener entre 4 y 15 caracteres.")
    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]*$", message = "El nombre solo puede contener letras.")
    private String name;

    @NotEmpty
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty(required = true)
    @Size(min = 4, max = 15, message = "El nombre debe tener entre 4 y 15 caracteres.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "El nombre solo puede contener letras.")
    @Column(name = "lastname")
    private String lastName;



    @NotNull
    @Column(nullable = false)
    private boolean registered;

    @ManyToOne
    @JoinColumn(name = "id_school")
    private School school;

    /*public Student() {

    }

    public Student(Long id, String name, String lastName, boolean registered) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.registered = registered;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
