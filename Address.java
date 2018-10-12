package com.objectfrontier.training.java.jdbc;

public class Address {

    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    private String street;

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    private String city;

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    private long postalCode;

    public long getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(long postalCode) {
        this.postalCode = postalCode;
    }
}
