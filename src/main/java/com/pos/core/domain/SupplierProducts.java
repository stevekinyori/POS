package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A SupplierProducts.
 */
@Entity
@Table(name = "supplier_products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "supplierproducts")
public class SupplierProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "buying_price", nullable = false)
    private Long buyingPrice;

    @Column(name = "standard_buying_price")
    private Long standardBuyingPrice;

    @NotNull
    @Column(name = "retail_price", nullable = false)
    private Long retailPrice;

    @Column(name = "delivery_lead_time")
    private Integer deliveryLeadTime;

    @Column(name = "first_item_delivery_date")
    private LocalDate firstItemDeliveryDate;

    @Column(name = "minimum_reorder_level")
    private Integer minimumReorderLevel;

    @Column(name = "max_reorder_level")
    private Integer maxReorderLevel;

    @OneToOne
    @JoinColumn(unique = true)
    private Products products;

    @OneToOne
    @JoinColumn(unique = true)
    private Suppliers supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyingPrice() {
        return buyingPrice;
    }

    public SupplierProducts buyingPrice(Long buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public void setBuyingPrice(Long buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Long getStandardBuyingPrice() {
        return standardBuyingPrice;
    }

    public SupplierProducts standardBuyingPrice(Long standardBuyingPrice) {
        this.standardBuyingPrice = standardBuyingPrice;
        return this;
    }

    public void setStandardBuyingPrice(Long standardBuyingPrice) {
        this.standardBuyingPrice = standardBuyingPrice;
    }

    public Long getRetailPrice() {
        return retailPrice;
    }

    public SupplierProducts retailPrice(Long retailPrice) {
        this.retailPrice = retailPrice;
        return this;
    }

    public void setRetailPrice(Long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Integer getDeliveryLeadTime() {
        return deliveryLeadTime;
    }

    public SupplierProducts deliveryLeadTime(Integer deliveryLeadTime) {
        this.deliveryLeadTime = deliveryLeadTime;
        return this;
    }

    public void setDeliveryLeadTime(Integer deliveryLeadTime) {
        this.deliveryLeadTime = deliveryLeadTime;
    }

    public LocalDate getFirstItemDeliveryDate() {
        return firstItemDeliveryDate;
    }

    public SupplierProducts firstItemDeliveryDate(LocalDate firstItemDeliveryDate) {
        this.firstItemDeliveryDate = firstItemDeliveryDate;
        return this;
    }

    public void setFirstItemDeliveryDate(LocalDate firstItemDeliveryDate) {
        this.firstItemDeliveryDate = firstItemDeliveryDate;
    }

    public Integer getMinimumReorderLevel() {
        return minimumReorderLevel;
    }

    public SupplierProducts minimumReorderLevel(Integer minimumReorderLevel) {
        this.minimumReorderLevel = minimumReorderLevel;
        return this;
    }

    public void setMinimumReorderLevel(Integer minimumReorderLevel) {
        this.minimumReorderLevel = minimumReorderLevel;
    }

    public Integer getMaxReorderLevel() {
        return maxReorderLevel;
    }

    public SupplierProducts maxReorderLevel(Integer maxReorderLevel) {
        this.maxReorderLevel = maxReorderLevel;
        return this;
    }

    public void setMaxReorderLevel(Integer maxReorderLevel) {
        this.maxReorderLevel = maxReorderLevel;
    }

    public Products getProducts() {
        return products;
    }

    public SupplierProducts products(Products products) {
        this.products = products;
        return this;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Suppliers getSupplier() {
        return supplier;
    }

    public SupplierProducts supplier(Suppliers suppliers) {
        this.supplier = suppliers;
        return this;
    }

    public void setSupplier(Suppliers suppliers) {
        this.supplier = suppliers;
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
        SupplierProducts supplierProducts = (SupplierProducts) o;
        if (supplierProducts.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), supplierProducts.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SupplierProducts{" +
            "id=" + getId() +
            ", buyingPrice=" + getBuyingPrice() +
            ", standardBuyingPrice=" + getStandardBuyingPrice() +
            ", retailPrice=" + getRetailPrice() +
            ", deliveryLeadTime=" + getDeliveryLeadTime() +
            ", firstItemDeliveryDate='" + getFirstItemDeliveryDate() + "'" +
            ", minimumReorderLevel=" + getMinimumReorderLevel() +
            ", maxReorderLevel=" + getMaxReorderLevel() +
            "}";
    }
}
