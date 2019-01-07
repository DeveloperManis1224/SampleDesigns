package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class LoginData {

    private String email;
    private String password;


   public LoginData(String username,String password)
    {
        this.email = username;
        this.password = password;

    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }
}
