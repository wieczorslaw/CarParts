package pl.ailux.carparts.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.ailux.carparts.domain.PartSellingArgument} entity.
 */
public class PartSellingArgumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String reason;


    private Long partId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

        PartSellingArgumentDTO partSellingArgumentDTO = (PartSellingArgumentDTO) o;
        if (partSellingArgumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partSellingArgumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PartSellingArgumentDTO{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", partId=" + getPartId() +
            "}";
    }
}
