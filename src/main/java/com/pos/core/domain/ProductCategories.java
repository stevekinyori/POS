package com.pos.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A ProductCategories.
 */
@Entity
@Table(name = "product_categories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productcategories")
public class ProductCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "sht_description", nullable = false)
    private String shtDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "date_created")
    private Instant dateCreated;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SubCategories> subCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public ProductCategories code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public ProductCategories name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShtDescription() {
        return shtDescription;
    }

    public ProductCategories shtDescription(String shtDescription) {
        this.shtDescription = shtDescription;
        return this;
    }

    public void setShtDescription(String shtDescription) {
        this.shtDescription = shtDescription;
    }

    public String getDescription() {
        return description;
    }

    public ProductCategories description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public ProductCategories dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<SubCategories> getSubCategories() {
        return subCategories;
    }

    public ProductCategories subCategories(Set<SubCategories> subCategories) {
        this.subCategories = subCategories;
        return this;
    }

    public ProductCategories addSubCategories(SubCategories subCategories) {
        this.subCategories.add(subCategories);
        subCategories.setCategory(this);
        return this;
    }

    public ProductCategories removeSubCategories(SubCategories subCategories) {
        this.subCategories.remove(subCategories);
        subCategories.setCategory(null);
        return this;
    }

    public void setSubCategories(Set<SubCategories> subCategories) {
        this.subCategories = subCategories;
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
        ProductCategories productCategories = (ProductCategories) o;
        if (productCategories.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategories.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategories{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shtDescription='" + getShtDescription() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
