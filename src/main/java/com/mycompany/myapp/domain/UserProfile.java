package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "baitdata", "fish", "weather", "user" }, allowSetters = true)
    private Set<CreatedCatch> catches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<CreatedCatch> getCatches() {
        return this.catches;
    }

    public void setCatches(Set<CreatedCatch> createdCatches) {
        if (this.catches != null) {
            this.catches.forEach(i -> i.setUser(null));
        }
        if (createdCatches != null) {
            createdCatches.forEach(i -> i.setUser(this));
        }
        this.catches = createdCatches;
    }

    public UserProfile catches(Set<CreatedCatch> createdCatches) {
        this.setCatches(createdCatches);
        return this;
    }

    public UserProfile addCatches(CreatedCatch createdCatch) {
        this.catches.add(createdCatch);
        createdCatch.setUser(this);
        return this;
    }

    public UserProfile removeCatches(CreatedCatch createdCatch) {
        this.catches.remove(createdCatch);
        createdCatch.setUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
