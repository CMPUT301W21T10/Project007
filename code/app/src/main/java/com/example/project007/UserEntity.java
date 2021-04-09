package com.example.project007;
/**
 * This class prepare an instance of a UserEntity
 * <p>
 * Store Uid, username, phone, email.<br>
 * </p>
 */
public class UserEntity {

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String uid ;
    private String username ;
    private String phone ;
    private String email ;
}
