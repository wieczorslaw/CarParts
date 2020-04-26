package pl.ailux.carparts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarPart.
 */
@Entity
@Table(name = "car_part")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarPart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @NotNull
    @Column(name = "shipping_time", nullable = false)
    private Integer shippingTime;

    @OneToMany(mappedBy = "part")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PartSellingArgument> partSellingArguments = new HashSet<>();

    @OneToMany(mappedBy = "part")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PartServiceAction> partServiceActions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("carParts")
    private CarModel model;

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

    public CarPart name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CarPart description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CarPart price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean isAvailable() {
        return available;
    }

    public CarPart available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getShippingTime() {
        return shippingTime;
    }

    public CarPart shippingTime(Integer shippingTime) {
        this.shippingTime = shippingTime;
        return this;
    }

    public void setShippingTime(Integer shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Set<PartSellingArgument> getPartSellingArguments() {
        return partSellingArguments;
    }

    public CarPart partSellingArguments(Set<PartSellingArgument> partSellingArguments) {
        this.partSellingArguments = partSellingArguments;
        return this;
    }

    public CarPart addPartSellingArgument(PartSellingArgument partSellingArgument) {
        this.partSellingArguments.add(partSellingArgument);
        partSellingArgument.setPart(this);
        return this;
    }

    public CarPart removePartSellingArgument(PartSellingArgument partSellingArgument) {
        this.partSellingArguments.remove(partSellingArgument);
        partSellingArgument.setPart(null);
        return this;
    }

    public void setPartSellingArguments(Set<PartSellingArgument> partSellingArguments) {
        this.partSellingArguments = partSellingArguments;
    }

    public Set<PartServiceAction> getPartServiceActions() {
        return partServiceActions;
    }

    public CarPart partServiceActions(Set<PartServiceAction> partServiceActions) {
        this.partServiceActions = partServiceActions;
        return this;
    }

    public CarPart addPartServiceAction(PartServiceAction partServiceAction) {
        this.partServiceActions.add(partServiceAction);
        partServiceAction.setPart(this);
        return this;
    }

    public CarPart removePartServiceAction(PartServiceAction partServiceAction) {
        this.partServiceActions.remove(partServiceAction);
        partServiceAction.setPart(null);
        return this;
    }

    public void setPartServiceActions(Set<PartServiceAction> partServiceActions) {
        this.partServiceActions = partServiceActions;
    }

    public CarModel getModel() {
        return model;
    }

    public CarPart model(CarModel carModel) {
        this.model = carModel;
        return this;
    }

    public void setModel(CarModel carModel) {
        this.model = carModel;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarPart)) {
            return false;
        }
        return id != null && id.equals(((CarPart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CarPart{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", available='" + isAvailable() + "'" +
            ", shippingTime=" + getShippingTime() +
            "}";
    }
}
