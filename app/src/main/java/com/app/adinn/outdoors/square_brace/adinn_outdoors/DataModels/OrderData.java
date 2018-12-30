package com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels;

public class OrderData {
    private String id;
    private  String image;
    private  String name;
    private String orderId;
    private String status;
    private String serialNumber;

    public OrderData(String id, String image, String name, String orderId, String status, String serialNumber) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.orderId = orderId;
        this.status = status;
        this.serialNumber = serialNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
