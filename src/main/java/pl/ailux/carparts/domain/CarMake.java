package pl.ailux.carparts.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarMake.
 */
@Entity
@Table(name = "car_make")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarMake implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "make")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CarModel> carModels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CarMake name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CarModel> getCarModels() {
        return carModels;
    }

    public CarMake carModels(Set<CarModel> carModels) {
        this.carModels = carModels;
        return this;
    }

    public CarMake addCarModel(CarModel carModel) {
        this.carModels.add(carModel);
        carModel.setMake(this);
        return this;
    }

    public CarMake removeCarModel(CarModel carModel) {
        this.carModels.remove(carModel);
        carModel.setMake(null);
        return this;
    }

    public void setCarModels(Set<CarModel> carModels) {
        this.carModels = carModels;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarMake)) {
            return false;
        }
        return id != null && id.equals(((CarMake) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CarMake{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
