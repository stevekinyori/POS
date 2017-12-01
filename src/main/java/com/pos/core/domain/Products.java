package com.pos.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "products")
public class Products implements Serializable {

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

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @Column(name = "average_monthly_usage")
    private Integer averageMonthlyUsage;

    @Column(name = "quantity_supplied_to_date")
    private Integer quantitySuppliedToDate;

    @OneToOne
    @JoinColumn(unique = true)
    private SubCategories subCategory;

    @OneToOne
    @JoinColumn(unique = true)
    private Brands brand;

    @OneToMany(mappedBy = "products")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Inventory> invRecords = new HashSet<>();

    @OneToOne(mappedBy = "products")
    @JsonIgnore
    private SupplierProducts suppliers;

    @OneToOne(mappedBy = "batchProduct")
    @JsonIgnore
    private GoodsReceivedFromSupplier receivedFromSupplier;

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

    public Products code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Products name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShtDescription() {
        return shtDescription;
    }

    public Products shtDescription(String shtDescription) {
        this.shtDescription = shtDescription;
        return this;
    }

    public void setShtDescription(String shtDescription) {
        this.shtDescription = shtDescription;
    }

    public String getDescription() {
        return description;
    }

    public Products description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public Products reorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
        return this;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public Products reorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
        return this;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Integer getAverageMonthlyUsage() {
        return averageMonthlyUsage;
    }

    public Products averageMonthlyUsage(Integer averageMonthlyUsage) {
        this.averageMonthlyUsage = averageMonthlyUsage;
        return this;
    }

    public void setAverageMonthlyUsage(Integer averageMonthlyUsage) {
        this.averageMonthlyUsage = averageMonthlyUsage;
    }

    public Integer getQuantitySuppliedToDate() {
        return quantitySuppliedToDate;
    }

    public Products quantitySuppliedToDate(Integer quantitySuppliedToDate) {
        this.quantitySuppliedToDate = quantitySuppliedToDate;
        return this;
    }

    public void setQuantitySuppliedToDate(Integer quantitySuppliedToDate) {
        this.quantitySuppliedToDate = quantitySuppliedToDate;
    }

    public SubCategories getSubCategory() {
        return subCategory;
    }

    public Products subCategory(SubCategories subCategories) {
        this.subCategory = subCategories;
        return this;
    }

    public void setSubCategory(SubCategories subCategories) {
        this.subCategory = subCategories;
    }

    public Brands getBrand() {
        return brand;
    }

    public Products brand(Brands brands) {
        this.brand = brands;
        return this;
    }

    public void setBrand(Brands brands) {
        this.brand = brands;
    }

    public Set<Inventory> getInvRecords() {
        return invRecords;
    }

    public Products invRecords(Set<Inventory> inventories) {
        this.invRecords = inventories;
        return this;
    }

    public Products addInvRecords(Inventory inventory) {
        this.invRecords.add(inventory);
        inventory.setProducts(this);
        return this;
    }

    public Products removeInvRecords(Inventory inventory) {
        this.invRecords.remove(inventory);
        inventory.setProducts(null);
        return this;
    }

    public void setInvRecords(Set<Inventory> inventories) {
        this.invRecords = inventories;
    }

    public SupplierProducts getSuppliers() {
        return suppliers;
    }

    public Products suppliers(SupplierProducts supplierProducts) {
        this.suppliers = supplierProducts;
        return this;
    }

    public void setSuppliers(SupplierProducts supplierProducts) {
        this.suppliers = supplierProducts;
    }

    public GoodsReceivedFromSupplier getReceivedFromSupplier() {
        return receivedFromSupplier;
    }

    public Products receivedFromSupplier(GoodsReceivedFromSupplier goodsReceivedFromSupplier) {
        this.receivedFromSupplier = goodsReceivedFromSupplier;
        return this;
    }

    public void setReceivedFromSupplier(GoodsReceivedFromSupplier goodsReceivedFromSupplier) {
        this.receivedFromSupplier = goodsReceivedFromSupplier;
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
        Products products = (Products) o;
        if (products.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), products.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", shtDescription='" + getShtDescription() + "'" +
            ", description='" + getDescription() + "'" +
            ", reorderLevel=" + getReorderLevel() +
            ", reorderQuantity=" + getReorderQuantity() +
            ", averageMonthlyUsage=" + getAverageMonthlyUsage() +
            ", quantitySuppliedToDate=" + getQuantitySuppliedToDate() +
            "}";
    }
}
