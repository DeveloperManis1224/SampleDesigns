package com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels;

public class PlaceModel {

    private String id;
    private  String place;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public PlaceModel(String id, String place) {
        this.id = id;
        this.place = place;
    }
}
