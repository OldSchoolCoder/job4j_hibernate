package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer experience;
    private Integer salary;

    public Candidate() {
    }

    public Candidate(String name, Integer experience, Integer salary) {
        this.name = name;
        this.experience = experience;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidate)) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(getId(), candidate.getId()) && Objects.equals(getName(),
                candidate.getName()) && Objects.equals(getExperience(),
                candidate.getExperience()) && Objects.equals(getSalary(),
                candidate.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getExperience(), getSalary());
    }
}
