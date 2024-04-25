package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BaitData.
 */
@Entity
@Table(name = "bait_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaitData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "color")
    private String color;

    @Column(name = "hard")
    private Boolean hard;

    @Column(name = "soft")
    private Boolean soft;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "baitdata")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "baitdata", "fish", "weather", "user" }, allowSetters = true)
    private Set<CreatedCatch> catches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BaitData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public BaitData type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return this.color;
    }

    public BaitData color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getHard() {
        return this.hard;
    }

    public BaitData hard(Boolean hard) {
        this.setHard(hard);
        return this;
    }

    public void setHard(Boolean hard) {
        this.hard = hard;
    }

    public Boolean getSoft() {
        return this.soft;
    }

    public BaitData soft(Boolean soft) {
        this.setSoft(soft);
        return this;
    }

    public void setSoft(Boolean soft) {
        this.soft = soft;
    }

    public Set<CreatedCatch> getCatches() {
        return this.catches;
    }

    public void setCatches(Set<CreatedCatch> createdCatches) {
        if (this.catches != null) {
            this.catches.forEach(i -> i.removeBaitdata(this));
        }
        if (createdCatches != null) {
            createdCatches.forEach(i -> i.addBaitdata(this));
        }
        this.catches = createdCatches;
    }

    public BaitData catches(Set<CreatedCatch> createdCatches) {
        this.setCatches(createdCatches);
        return this;
    }

    public BaitData addCatches(CreatedCatch createdCatch) {
        this.catches.add(createdCatch);
        createdCatch.getBaitdata().add(this);
        return this;
    }

    public BaitData removeCatches(CreatedCatch createdCatch) {
        this.catches.remove(createdCatch);
        createdCatch.getBaitdata().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaitData)) {
            return false;
        }
        return getId() != null && getId().equals(((BaitData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaitData{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", color='" + getColor() + "'" +
            ", hard='" + getHard() + "'" +
            ", soft='" + getSoft() + "'" +
            "}";
    }
}
