package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

public class GetDealResponse {



    private String productname;
    private String percentage;
    private String validfrom;
    private String validtill;
    private String id;

    public GetDealResponse(String productname, String percentage, String validfrom, String validtill, String id) {
        this.productname = productname;
        this.percentage = percentage;
        this.validfrom = validfrom;
        this.validtill = validtill;
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public String getValidtill() {
        return validtill;
    }

    public String getId() {
        return id;
    }
}
