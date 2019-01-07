package com.app.adinn.outdoors.square_brace.adinn_outdoors.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedDealData {


    @SerializedName("productname")
    @Expose
    private String productname;
    @SerializedName("dealname")
    @Expose
    private String dealname;
    @SerializedName("offertype")
    @Expose
    private String offertype;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("validfrom")
    @Expose
    private String validfrom;
    @SerializedName("validtill")
    @Expose
    private String validtill;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("shopname")
    @Expose
    private String shopname;
    @SerializedName("shopid")
    @Expose
    private String shopid;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("offercode")
    @Expose
    private String offercode;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;


    public FeaturedDealData(String productname, String dealname, String offertype, String offer, String validfrom, String validtill, String description, String shopname, String shopid, String image, String offercode, String category, String status, Integer id) {
        this.productname = productname;
        this.dealname = dealname;
        this.offertype = offertype;
        this.offer = offer;
        this.validfrom = validfrom;
        this.validtill = validtill;
        this.description = description;
        this.shopname = shopname;
        this.shopid = shopid;
        this.image = image;
        this.offercode = offercode;
        this.category = category;
        this.status = status;
        this.id = id;
    }


    public String getProductname() {
        return productname;
    }

    public String getDealname() {
        return dealname;
    }

    public String getOffertype() {
        return offertype;
    }

    public String getOffer() {
        return offer;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public String getValidtill() {
        return validtill;
    }

    public String getDescription() {
        return description;
    }

    public String getShopname() {
        return shopname;
    }

    public String getShopid() {
        return shopid;
    }

    public String getImage() {
        return image;
    }

    public String getOffercode() {
        return offercode;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }
}
