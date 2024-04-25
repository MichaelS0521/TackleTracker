package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FishData.
 */
@Entity
@Table(name = "fish_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FishData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "water_type")
    private String waterType;

    @Column(name = "weight")
    private Float weight;

    @JsonIgnoreProperties(value = { "baitdata", "fish", "weather", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CreatedCatch createdCatch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FishData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FishData name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWaterType() {
        return this.waterType;
    }

    public FishData waterType(String waterType) {
        this.setWaterType(waterType);
        return this;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public Float getWeight() {
        return this.weight;
    }

    public FishData weight(Float weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public CreatedCatch getCreatedCatch() {
        return this.createdCatch;
    }

    public void setCreatedCatch(CreatedCatch createdCatch) {
        this.createdCatch = createdCatch;
    }

    public FishData createdCatch(CreatedCatch createdCatch) {
        this.setCreatedCatch(createdCatch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FishData)) {
            return false;
        }
        return getId() != null && getId().equals(((FishData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FishData{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", waterType='" + getWaterType() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}
