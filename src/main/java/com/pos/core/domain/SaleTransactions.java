package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A SaleTransactions.
 */
@Entity
@Table(name = "sale_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "saletransactions")
public class SaleTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1)
    @Column(name = "quantity")
    private Integer quantity;

    @Min(value = 0)
    @Column(name = "unit_price")
    private Integer unitPrice;

    @ManyToOne
    private Inventory product;

    @ManyToOne
    private Sales sales;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public SaleTransactions quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public SaleTransactions unitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Inventory getProduct() {
        return product;
    }

    public SaleTransactions product(Inventory inventory) {
        this.product = inventory;
        return this;
    }

    public void setProduct(Inventory inventory) {
        this.product = inventory;
    }

    public Sales getSales() {
        return sales;
    }

    public SaleTransactions sales(Sales sales) {
        this.sales = sales;
        return this;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
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
        SaleTransactions saleTransactions = (SaleTransactions) o;
        if (saleTransactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleTransactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleTransactions{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            "}";
    }
}
