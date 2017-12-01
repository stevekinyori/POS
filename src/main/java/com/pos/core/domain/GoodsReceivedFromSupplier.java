package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A GoodsReceivedFromSupplier.
 */
@Entity
@Table(name = "goods_received_from_supplier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "goodsreceivedfromsupplier")
public class GoodsReceivedFromSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private Packaging pack;

    @OneToOne
    @JoinColumn(unique = true)
    private Products batchProduct;

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

    public GoodsReceivedFromSupplier batchNo(String batchNo) {
        this.batchNo = batchNo;
        return this;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public GoodsReceivedFromSupplier quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public GoodsReceivedFromSupplier unitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Packaging getPack() {
        return pack;
    }

    public GoodsReceivedFromSupplier pack(Packaging packaging) {
        this.pack = packaging;
        return this;
    }

    public void setPack(Packaging packaging) {
        this.pack = packaging;
    }

    public Products getBatchProduct() {
        return batchProduct;
    }

    public GoodsReceivedFromSupplier batchProduct(Products products) {
        this.batchProduct = products;
        return this;
    }

    public void setBatchProduct(Products products) {
        this.batchProduct = products;
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
        GoodsReceivedFromSupplier goodsReceivedFromSupplier = (GoodsReceivedFromSupplier) o;
        if (goodsReceivedFromSupplier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goodsReceivedFromSupplier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GoodsReceivedFromSupplier{" +
            "id=" + getId() +
            ", batchNo='" + getBatchNo() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            "}";
    }
}
