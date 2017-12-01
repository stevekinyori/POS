package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A GoodsReturnedToSupplier.
 */
@Entity
@Table(name = "goods_returned_to_supplier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "goodsreturnedtosupplier")
public class GoodsReturnedToSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToOne
    @JoinColumn(unique = true)
    private Suppliers batchSupplier;

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

    public GoodsReturnedToSupplier batchNo(String batchNo) {
        this.batchNo = batchNo;
        return this;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public GoodsReturnedToSupplier quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Suppliers getBatchSupplier() {
        return batchSupplier;
    }

    public GoodsReturnedToSupplier batchSupplier(Suppliers suppliers) {
        this.batchSupplier = suppliers;
        return this;
    }

    public void setBatchSupplier(Suppliers suppliers) {
        this.batchSupplier = suppliers;
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
        GoodsReturnedToSupplier goodsReturnedToSupplier = (GoodsReturnedToSupplier) o;
        if (goodsReturnedToSupplier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goodsReturnedToSupplier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GoodsReturnedToSupplier{" +
            "id=" + getId() +
            ", batchNo='" + getBatchNo() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
