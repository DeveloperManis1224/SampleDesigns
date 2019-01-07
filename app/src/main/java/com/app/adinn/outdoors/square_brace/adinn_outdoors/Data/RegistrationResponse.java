package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class RegistrationResponse {

    private String uId;
    private String timestamp;
    private String status;
    private String error;
    private String message;

    public RegistrationResponse(String user_id,String timestamp, String status, String error, String message) {
        this.uId = user_id;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
