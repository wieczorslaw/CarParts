package pl.ailux.carparts.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.ailux.carparts.domain.CarModel} entity.
 */
public class CarModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant productionStartDate;

    @NotNull
    private Instant productionEndDate;


    private Long makeId;

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

    public Instant getProductionStartDate() {
        return productionStartDate;
    }

    public void setProductionStartDate(Instant productionStartDate) {
        this.productionStartDate = productionStartDate;
    }

    public Instant getProductionEndDate() {
        return productionEndDate;
    }

    public void setProductionEndDate(Instant productionEndDate) {
        this.productionEndDate = productionEndDate;
    }

    public Long getMakeId() {
        return makeId;
    }

    public void setMakeId(Long carMakeId) {
        this.makeId = carMakeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarModelDTO carModelDTO = (CarModelDTO) o;
        if (carModelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carModelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", productionStartDate='" + getProductionStartDate() + "'" +
            ", productionEndDate='" + getProductionEndDate() + "'" +
            ", makeId=" + getMakeId() +
            "}";
    }
}
