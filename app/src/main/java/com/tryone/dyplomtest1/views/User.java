package com.tryone.dyplomtest1.views;

public class User {
    public String firstName, secName,id,email;

    public User() {
    }

    public User(String firstName, String secName, String id, String email) {
        this.firstName = firstName;
        this.secName = secName;
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
