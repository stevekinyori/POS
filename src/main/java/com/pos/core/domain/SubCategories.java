package com.pos.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A SubCategories.
 */
@Entity
@Table(name = "sub_categories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subcategories")
public class SubCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "short_desciption", nullable = false)
    private String shortDesciption;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "subCategory")
    @JsonIgnore
    private Products subCategoryProducts;

    @ManyToOne
    private ProductCategories category;

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

    public SubCategories name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesciption() {
        return shortDesciption;
    }

    public SubCategories shortDesciption(String shortDesciption) {
        this.shortDesciption = shortDesciption;
        return this;
    }

    public void setShortDesciption(String shortDesciption) {
        this.shortDesciption = shortDesciption;
    }

    public String getDescription() {
        return description;
    }

    public SubCategories description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Products getSubCategoryProducts() {
        return subCategoryProducts;
    }

    public SubCategories subCategoryProducts(Products products) {
        this.subCategoryProducts = products;
        return this;
    }

    public void setSubCategoryProducts(Products products) {
        this.subCategoryProducts = products;
    }

    public ProductCategories getCategory() {
        return category;
    }

    public SubCategories category(ProductCategories productCategories) {
        this.category = productCategories;
        return this;
    }

    public void setCategory(ProductCategories productCategories) {
        this.category = productCategories;
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
        SubCategories subCategories = (SubCategories) o;
        if (subCategories.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subCategories.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubCategories{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortDesciption='" + getShortDesciption() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
