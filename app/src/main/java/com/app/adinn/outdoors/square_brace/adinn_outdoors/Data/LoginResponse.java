package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class LoginResponse {


    private String message;
    private String status;
    private String id;
    private String contact;
    private String dob;
    private String email;
    private String name;


    public LoginResponse(String message, String status, String id, String contact, String dob,
                         String email, String name) {
        this.message = message;
        this.status = status;
        this.id = id;
        this.contact = contact;
        this.dob = dob;
        this.email = email;
        this.name = name;
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
