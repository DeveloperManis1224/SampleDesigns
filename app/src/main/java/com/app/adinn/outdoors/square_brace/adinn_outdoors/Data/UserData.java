package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class UserData {
    private String id;
    private String name;
    private String gender;
    private String contact;
    private String dob;
    private String email;
    private String password;
    private String anniversary;
    public UserData(String name, String gender, String contact, String dob, String email,
                    String password , String anniversary) {
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.anniversary = anniversary;
    }

    public UserData(String id, String name, String gender, String contact, String dob, String email, String password, String anniversary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.anniversary = anniversary;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
