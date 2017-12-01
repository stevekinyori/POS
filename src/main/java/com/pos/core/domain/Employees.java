package com.pos.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.pos.core.domain.enumeration.GENDER;

import com.pos.core.domain.enumeration.Banks;

import com.pos.core.domain.enumeration.PAYMENT_TYPES;


/**
 * A Employees.
 */
@Entity
@Table(name = "employees")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employees")
public class Employees implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "id_no", nullable = false)
    private Integer idNo;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GENDER gender;

    @NotNull
    @Column(name = "estate", nullable = false)
    private String estate;

    @Column(name = "apartment_no")
    private String apartmentNo;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "bank_name", nullable = false)
    private Banks bankName;

    @NotNull
    @Column(name = "account_no", nullable = false)
    private String accountNo;

    @NotNull
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PAYMENT_TYPES paymentMode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employees firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employees lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getIdNo() {
        return idNo;
    }

    public Employees idNo(Integer idNo) {
        this.idNo = idNo;
        return this;
    }

    public void setIdNo(Integer idNo) {
        this.idNo = idNo;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Employees dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public Employees age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public GENDER getGender() {
        return gender;
    }

    public Employees gender(GENDER gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getEstate() {
        return estate;
    }

    public Employees estate(String estate) {
        this.estate = estate;
        return this;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public Employees apartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
        return this;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getEmail() {
        return email;
    }

    public Employees email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Banks getBankName() {
        return bankName;
    }

    public Employees bankName(Banks bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(Banks bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public Employees accountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public Employees accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public PAYMENT_TYPES getPaymentMode() {
        return paymentMode;
    }

    public Employees paymentMode(PAYMENT_TYPES paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    public void setPaymentMode(PAYMENT_TYPES paymentMode) {
        this.paymentMode = paymentMode;
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
        Employees employees = (Employees) o;
        if (employees.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employees.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employees{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", idNo=" + getIdNo() +
            ", dob='" + getDob() + "'" +
            ", age=" + getAge() +
            ", gender='" + getGender() + "'" +
            ", estate='" + getEstate() + "'" +
            ", apartmentNo='" + getApartmentNo() + "'" +
            ", email='" + getEmail() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", accountNo='" + getAccountNo() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", paymentMode='" + getPaymentMode() + "'" +
            "}";
    }
}
