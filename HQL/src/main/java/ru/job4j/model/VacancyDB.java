package ru.job4j.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vacancy_db")
public class VacancyDB implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacancy> vacancies = new ArrayList<>();

    public VacancyDB() {
    }

    public VacancyDB(String name, List<Vacancy> vacancies) {
        this.name = name;
        this.vacancies = vacancies;
    }

    public void addVacancy(Vacancy vacancy) {
        this.vacancies.add(vacancy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VacancyDB)) return false;
        VacancyDB vacancyDB = (VacancyDB) o;
        return  Objects.equals(id, vacancyDB.id) &&
                Objects.equals(name, vacancyDB.name) &&
                Objects.equals(vacancies, vacancyDB.vacancies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vacancies);
    }
}
