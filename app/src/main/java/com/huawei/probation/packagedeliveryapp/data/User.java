package com.huawei.probation.packagedeliveryapp.data;

public class User {

    private String clientName;
    private String clientLastname;
    private String password;
    private String email;
    private String clientCity;
    private String clientPhone;

    public User() {

    }

    public User(String clientName, String clientLastname, String password, String email, String clientCity, String clientPhone) {
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.password = password;
        this.email = email;
        this.clientCity = clientCity;
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastname() {
        return clientLastname;
    }

    public void setClientLastname(String clientLastname) {
        this.clientLastname = clientLastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
