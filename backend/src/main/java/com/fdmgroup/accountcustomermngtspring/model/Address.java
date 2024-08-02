package com.fdmgroup.accountcustomermngtspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Address {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;
	@Column(nullable = false, updatable = false)
	@NotBlank(message = "Street number is mandatory")
    private String streetNumber;
	@Column(nullable = false)
	@NotBlank(message = "Postal Code is mandatory")
    private String postalCode;
	@Column(nullable = false)
	@NotBlank(message = "City is mandatory")
    private String city;
	@Column(nullable = false)
	@NotBlank(message = "Province is mandatory")
    private String province;
    
    Address(){}

    public Address(String streetNumber, String postalCode, String city, String province) {
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.province = province;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
