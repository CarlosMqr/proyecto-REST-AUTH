package com.course.REST.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "school")
public class School {
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
    @Size(min = 4, max = 15, message = "El estado debe tener entre 4 y 15 caracteres.")
    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]*$", message = "El estado solo puede contener letras.")
    private String state;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
