package pl.ailux.carparts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PartSellingArgument.
 */
@Entity
@Table(name = "part_selling_argument")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PartSellingArgument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @ManyToOne
    @JsonIgnoreProperties("partSellingArguments")
    private CarPart part;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public PartSellingArgument reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CarPart getPart() {
        return part;
    }

    public PartSellingArgument part(CarPart carPart) {
        this.part = carPart;
        return this;
    }

    public void setPart(CarPart carPart) {
        this.part = carPart;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartSellingArgument)) {
            return false;
        }
        return id != null && id.equals(((PartSellingArgument) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PartSellingArgument{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
