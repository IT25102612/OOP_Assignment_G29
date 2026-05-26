package com.tranquility.models;

public class Customer {

    //attributes
    private String customerId;
    private String name;
    private String nic;
    private String phone;
    private String email;

    //constructor
    public Customer(String customerId, String name, String nic, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getNic() {
        return nic;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return customerId + "," + name + "," + nic + "," + phone + "," + email;
    }
}
