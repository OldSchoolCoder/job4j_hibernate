package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "car", schema = "public", catalog = "car")
public class CarEntity {

    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private EngineEntity engineId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "history_owner", joinColumns = {
            @JoinColumn(name = "driver_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "car_id",
                    nullable = false, updatable = false)})
    private Set<DriverEntity> drivers;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<DriverEntity> getDrivers() {
        return drivers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarEntity)) return false;
        CarEntity carEntity = (CarEntity) o;
        return Objects.equals(getId(), carEntity.getId()) && Objects.equals(engineId, carEntity.engineId) && Objects.equals(getDrivers(), carEntity.getDrivers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), engineId, getDrivers());
    }
}
