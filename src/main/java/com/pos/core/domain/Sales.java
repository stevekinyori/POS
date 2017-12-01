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
 * A Sales.
 */
@Entity
@Table(name = "sales")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sales")
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_initiated", nullable = false)
    private Instant dateInitiated;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_sale_amount")
    private Integer totalSaleAmount;

    @Column(name = "total_paid_amount")
    private Integer totalPaidAmount;

    @Column(name = "date_closed")
    private Instant dateClosed;

    @OneToMany(mappedBy = "sales")
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

    public Instant getDateInitiated() {
        return dateInitiated;
    }

    public Sales dateInitiated(Instant dateInitiated) {
        this.dateInitiated = dateInitiated;
        return this;
    }

    public void setDateInitiated(Instant dateInitiated) {
        this.dateInitiated = dateInitiated;
    }

    public Integer getUserId() {
        return userId;
    }

    public Sales userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public Sales totalSaleAmount(Integer totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
        return this;
    }

    public void setTotalSaleAmount(Integer totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public Integer getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public Sales totalPaidAmount(Integer totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
        return this;
    }

    public void setTotalPaidAmount(Integer totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public Instant getDateClosed() {
        return dateClosed;
    }

    public Sales dateClosed(Instant dateClosed) {
        this.dateClosed = dateClosed;
        return this;
    }

    public void setDateClosed(Instant dateClosed) {
        this.dateClosed = dateClosed;
    }

    public Set<SaleTransactions> getTransactions() {
        return transactions;
    }

    public Sales transactions(Set<SaleTransactions> saleTransactions) {
        this.transactions = saleTransactions;
        return this;
    }

    public Sales addTransactions(SaleTransactions saleTransactions) {
        this.transactions.add(saleTransactions);
        saleTransactions.setSales(this);
        return this;
    }

    public Sales removeTransactions(SaleTransactions saleTransactions) {
        this.transactions.remove(saleTransactions);
        saleTransactions.setSales(null);
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
        Sales sales = (Sales) o;
        if (sales.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sales.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sales{" +
            "id=" + getId() +
            ", dateInitiated='" + getDateInitiated() + "'" +
            ", userId=" + getUserId() +
            ", totalSaleAmount=" + getTotalSaleAmount() +
            ", totalPaidAmount=" + getTotalPaidAmount() +
            ", dateClosed='" + getDateClosed() + "'" +
            "}";
    }
}
