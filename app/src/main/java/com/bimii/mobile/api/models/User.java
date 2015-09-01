package com.bimii.mobile.api.models;

public final class User {

    public String email;
    public String password;
    public String unique_device_id;

    public User(String email, String password, String unique_device_id) {
        this.email = email;
        this.password = password;
        this.unique_device_id = unique_device_id;
    }
}
