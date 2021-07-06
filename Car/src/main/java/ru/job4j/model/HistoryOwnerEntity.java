package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "history_owner", schema = "public", catalog = "car")
public class HistoryOwnerEntity {
    private Integer id;
    private Integer driverId;
    private Integer carId;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "driver_id")
    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "car_id")
    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryOwnerEntity that = (HistoryOwnerEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(driverId, that.driverId) && Objects.equals(carId, that.carId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, driverId, carId);
    }
}
