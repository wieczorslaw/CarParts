package pl.ailux.carparts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarModel.
 */
@Entity
@Table(name = "car_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "production_start_date", nullable = false)
    private Instant productionStartDate;

    @NotNull
    @Column(name = "production_end_date", nullable = false)
    private Instant productionEndDate;

    @OneToMany(mappedBy = "model")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CarPart> carParts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("carModels")
    private CarMake make;

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

    public CarModel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getProductionStartDate() {
        return productionStartDate;
    }

    public CarModel productionStartDate(Instant productionStartDate) {
        this.productionStartDate = productionStartDate;
        return this;
    }

    public void setProductionStartDate(Instant productionStartDate) {
        this.productionStartDate = productionStartDate;
    }

    public Instant getProductionEndDate() {
        return productionEndDate;
    }

    public CarModel productionEndDate(Instant productionEndDate) {
        this.productionEndDate = productionEndDate;
        return this;
    }

    public void setProductionEndDate(Instant productionEndDate) {
        this.productionEndDate = productionEndDate;
    }

    public Set<CarPart> getCarParts() {
        return carParts;
    }

    public CarModel carParts(Set<CarPart> carParts) {
        this.carParts = carParts;
        return this;
    }

    public CarModel addCarPart(CarPart carPart) {
        this.carParts.add(carPart);
        carPart.setModel(this);
        return this;
    }

    public CarModel removeCarPart(CarPart carPart) {
        this.carParts.remove(carPart);
        carPart.setModel(null);
        return this;
    }

    public void setCarParts(Set<CarPart> carParts) {
        this.carParts = carParts;
    }

    public CarMake getMake() {
        return make;
    }

    public CarModel make(CarMake carMake) {
        this.make = carMake;
        return this;
    }

    public void setMake(CarMake carMake) {
        this.make = carMake;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarModel)) {
            return false;
        }
        return id != null && id.equals(((CarModel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CarModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", productionStartDate='" + getProductionStartDate() + "'" +
            ", productionEndDate='" + getProductionEndDate() + "'" +
            "}";
    }
}
