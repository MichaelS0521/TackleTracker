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
 * A CreatedCatch.
 */
@Entity
@Table(name = "created_catch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreatedCatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "datestamp")
    private LocalDate datestamp;

    @Column(name = "location")
    private String location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_created_catch__baitdata",
        joinColumns = @JoinColumn(name = "created_catch_id"),
        inverseJoinColumns = @JoinColumn(name = "baitdata_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "catches" }, allowSetters = true)
    private Set<BaitData> baitdata = new HashSet<>();

    @JsonIgnoreProperties(value = { "createdCatch" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "createdCatch")
    private FishData fish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdCatches" }, allowSetters = true)
    private WeatherData weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "catches" }, allowSetters = true)
    private UserProfile user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CreatedCatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatestamp() {
        return this.datestamp;
    }

    public CreatedCatch datestamp(LocalDate datestamp) {
        this.setDatestamp(datestamp);
        return this;
    }

    public void setDatestamp(LocalDate datestamp) {
        this.datestamp = datestamp;
    }

    public String getLocation() {
        return this.location;
    }

    public CreatedCatch location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<BaitData> getBaitdata() {
        return this.baitdata;
    }

    public void setBaitdata(Set<BaitData> baitData) {
        this.baitdata = baitData;
    }

    public CreatedCatch baitdata(Set<BaitData> baitData) {
        this.setBaitdata(baitData);
        return this;
    }

    public CreatedCatch addBaitdata(BaitData baitData) {
        this.baitdata.add(baitData);
        return this;
    }

    public CreatedCatch removeBaitdata(BaitData baitData) {
        this.baitdata.remove(baitData);
        return this;
    }

    public FishData getFish() {
        return this.fish;
    }

    public void setFish(FishData fishData) {
        if (this.fish != null) {
            this.fish.setCreatedCatch(null);
        }
        if (fishData != null) {
            fishData.setCreatedCatch(this);
        }
        this.fish = fishData;
    }

    public CreatedCatch fish(FishData fishData) {
        this.setFish(fishData);
        return this;
    }

    public WeatherData getWeather() {
        return this.weather;
    }

    public void setWeather(WeatherData weatherData) {
        this.weather = weatherData;
    }

    public CreatedCatch weather(WeatherData weatherData) {
        this.setWeather(weatherData);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public CreatedCatch user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatedCatch)) {
            return false;
        }
        return getId() != null && getId().equals(((CreatedCatch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreatedCatch{" +
            "id=" + getId() +
            ", datestamp='" + getDatestamp() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
