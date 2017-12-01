package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;


/**
 * A COMPANY_DETAILS.
 */
@Entity
@Table(name = "company_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company_details")
public class COMPANY_DETAILS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Column(name = "date_opened", nullable = false)
    private Instant dateOpened;

    @NotNull
    @Column(name = "licence_no", nullable = false)
    private String licenceNo;

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

    public COMPANY_DETAILS name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public COMPANY_DETAILS location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getDateOpened() {
        return dateOpened;
    }

    public COMPANY_DETAILS dateOpened(Instant dateOpened) {
        this.dateOpened = dateOpened;
        return this;
    }

    public void setDateOpened(Instant dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public COMPANY_DETAILS licenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
        return this;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
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
        COMPANY_DETAILS cOMPANY_DETAILS = (COMPANY_DETAILS) o;
        if (cOMPANY_DETAILS.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cOMPANY_DETAILS.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "COMPANY_DETAILS{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", dateOpened='" + getDateOpened() + "'" +
            ", licenceNo='" + getLicenceNo() + "'" +
            "}";
    }
}
