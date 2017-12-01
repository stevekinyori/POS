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
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "batch_no", nullable = false)
    private String batchNo;

    @NotNull
    @Column(name = "bar_code_no", nullable = false)
    private String barCodeNo;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Min(value = 0)
    @Column(name = "sold_to_date")
    private Integer soldToDate;

    @Min(value = 0)
    @Column(name = "available_items")
    private Integer availableItems;

    @NotNull
    @Min(value = 0)
    @Column(name = "unit_selling_price", nullable = false)
    private Integer unitSellingPrice;

    @NotNull
    @Min(value = 0)
    @Column(name = "unit_buying_price", nullable = false)
    private Integer unitBuyingPrice;

    @ManyToOne
    private Products products;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SaleTransactions> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public Inventory batchNo(String batchNo) {
        this.batchNo = batchNo;
        return this;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBarCodeNo() {
        return barCodeNo;
    }

    public Inventory barCodeNo(String barCodeNo) {
        this.barCodeNo = barCodeNo;
        return this;
    }

    public void setBarCodeNo(String barCodeNo) {
        this.barCodeNo = barCodeNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Inventory quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldToDate() {
        return soldToDate;
    }

    public Inventory soldToDate(Integer soldToDate) {
        this.soldToDate = soldToDate;
        return this;
    }

    public void setSoldToDate(Integer soldToDate) {
        this.soldToDate = soldToDate;
    }

    public Integer getAvailableItems() {
        return availableItems;
    }

    public Inventory availableItems(Integer availableItems) {
        this.availableItems = availableItems;
        return this;
    }

    public void setAvailableItems(Integer availableItems) {
        this.availableItems = availableItems;
    }

    public Integer getUnitSellingPrice() {
        return unitSellingPrice;
    }

    public Inventory unitSellingPrice(Integer unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
        return this;
    }

    public void setUnitSellingPrice(Integer unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
    }

    public Integer getUnitBuyingPrice() {
        return unitBuyingPrice;
    }

    public Inventory unitBuyingPrice(Integer unitBuyingPrice) {
        this.unitBuyingPrice = unitBuyingPrice;
        return this;
    }

    public void setUnitBuyingPrice(Integer unitBuyingPrice) {
        this.unitBuyingPrice = unitBuyingPrice;
    }

    public Products getProducts() {
        return products;
    }

    public Inventory products(Products products) {
        this.products = products;
        return this;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Set<SaleTransactions> getTransactions() {
        return transactions;
    }

    public Inventory transactions(Set<SaleTransactions> saleTransactions) {
        this.transactions = saleTransactions;
        return this;
    }

    public Inventory addTransactions(SaleTransactions saleTransactions) {
        this.transactions.add(saleTransactions);
        saleTransactions.setProduct(this);
        return this;
    }

    public Inventory removeTransactions(SaleTransactions saleTransactions) {
        this.transactions.remove(saleTransactions);
        saleTransactions.setProduct(null);
        return this;
    }

    public void setTransactions(Set<SaleTransactions> saleTransactions) {
        this.transactions = saleTransactions;
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
        Inventory inventory = (Inventory) o;
        if (inventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", batchNo='" + getBatchNo() + "'" +
            ", barCodeNo='" + getBarCodeNo() + "'" +
            ", quantity=" + getQuantity() +
            ", soldToDate=" + getSoldToDate() +
            ", availableItems=" + getAvailableItems() +
            ", unitSellingPrice=" + getUnitSellingPrice() +
            ", unitBuyingPrice=" + getUnitBuyingPrice() +
            "}";
    }
}
