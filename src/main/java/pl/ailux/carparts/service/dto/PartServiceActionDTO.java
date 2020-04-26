package pl.ailux.carparts.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.ailux.carparts.domain.PartServiceAction} entity.
 */
public class PartServiceActionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;


    private Long partId;

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

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long carPartId) {
        this.partId = carPartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartServiceActionDTO partServiceActionDTO = (PartServiceActionDTO) o;
        if (partServiceActionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partServiceActionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PartServiceActionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", partId=" + getPartId() +
            "}";
    }
}
