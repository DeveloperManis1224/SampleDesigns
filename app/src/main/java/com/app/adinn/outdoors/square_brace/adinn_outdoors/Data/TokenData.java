package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class TokenData {


    private String userid;
    private  String Token;
    public TokenData(String userid, String token) {
        this.userid = userid;
        Token = token;
    }

    public String getUserid() {
        return userid;
    }

    public String getToken() {
        return Token;
    }
}
