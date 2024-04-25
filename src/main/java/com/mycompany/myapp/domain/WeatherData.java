package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WeatherData.
 */
@Entity
@Table(name = "weather_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeatherData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_condition")
    private String condition;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "date_stamp")
    private LocalDate dateStamp;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "weather")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "baitdata", "fish", "weather", "user" }, allowSetters = true)
    private Set<CreatedCatch> createdCatches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeatherData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return this.condition;
    }

    public WeatherData condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public WeatherData temperature(Double temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public LocalDate getDateStamp() {
        return this.dateStamp;
    }

    public WeatherData dateStamp(LocalDate dateStamp) {
        this.setDateStamp(dateStamp);
        return this;
    }

    public void setDateStamp(LocalDate dateStamp) {
        this.dateStamp = dateStamp;
    }

    public Set<CreatedCatch> getCreatedCatches() {
        return this.createdCatches;
    }

    public void setCreatedCatches(Set<CreatedCatch> createdCatches) {
        if (this.createdCatches != null) {
            this.createdCatches.forEach(i -> i.setWeather(null));
        }
        if (createdCatches != null) {
            createdCatches.forEach(i -> i.setWeather(this));
        }
        this.createdCatches = createdCatches;
    }

    public WeatherData createdCatches(Set<CreatedCatch> createdCatches) {
        this.setCreatedCatches(createdCatches);
        return this;
    }

    public WeatherData addCreatedCatch(CreatedCatch createdCatch) {
        this.createdCatches.add(createdCatch);
        createdCatch.setWeather(this);
        return this;
    }

    public WeatherData removeCreatedCatch(CreatedCatch createdCatch) {
        this.createdCatches.remove(createdCatch);
        createdCatch.setWeather(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherData)) {
            return false;
        }
        return getId() != null && getId().equals(((WeatherData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeatherData{" +
            "id=" + getId() +
            ", condition='" + getCondition() + "'" +
            ", temperature=" + getTemperature() +
            ", dateStamp='" + getDateStamp() + "'" +
            "}";
    }
}
