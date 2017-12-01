package com.pos.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Brands.
 */
@Entity
@Table(name = "brands")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "brands")
public class Brands implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sht_desc")
    private String shtDesc;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "brand")
    @JsonIgnore
    private Products products;

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

    public Brands name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShtDesc() {
        return shtDesc;
    }

    public Brands shtDesc(String shtDesc) {
        this.shtDesc = shtDesc;
        return this;
    }

    public void setShtDesc(String shtDesc) {
        this.shtDesc = shtDesc;
    }

    public String getDescription() {
        return description;
    }

    public Brands description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Products getProducts() {
        return products;
    }

    public Brands products(Products products) {
        this.products = products;
        return this;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Brands brands = (Brands) o;
        if (brands.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brands.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Brands{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shtDesc='" + getShtDesc() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
