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
 * A Suppliers.
 */
@Entity
@Table(name = "suppliers")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "suppliers")
public class Suppliers implements Serializable {

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
    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "supplier")
    @JsonIgnore
    private SupplierProducts products;

    @OneToOne(mappedBy = "batchSupplier")
    @JsonIgnore
    private GoodsReturnedToSupplier goodsReturned;

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

    public Suppliers code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Suppliers name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public Suppliers mobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLocation() {
        return location;
    }

    public Suppliers location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public Suppliers email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SupplierProducts getProducts() {
        return products;
    }

    public Suppliers products(SupplierProducts supplierProducts) {
        this.products = supplierProducts;
        return this;
    }

    public void setProducts(SupplierProducts supplierProducts) {
        this.products = supplierProducts;
    }

    public GoodsReturnedToSupplier getGoodsReturned() {
        return goodsReturned;
    }

    public Suppliers goodsReturned(GoodsReturnedToSupplier goodsReturnedToSupplier) {
        this.goodsReturned = goodsReturnedToSupplier;
        return this;
    }

    public void setGoodsReturned(GoodsReturnedToSupplier goodsReturnedToSupplier) {
        this.goodsReturned = goodsReturnedToSupplier;
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
        Suppliers suppliers = (Suppliers) o;
        if (suppliers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suppliers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Suppliers{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", mobileNo='" + getMobileNo() + "'" +
            ", location='" + getLocation() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
