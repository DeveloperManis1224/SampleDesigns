package com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Constants  {
    public static final String APP_NAME = "Adinn Outdoors";
    public static final String APP_BASE_URL = "http://adinn.candyrestaurant.com/images/product/";
    public static final String LOGIN_STATUS = "status";
    public static final String LOGIN = "1";
    public static final String LOGOUT = "0";
    public static final String CURRENT_USER_NAME = "username";
    public static final String CURRENT_USER_ID = "userid";
    public static final String CURRENT_USER_EMAIL = "useremail";
    public static final String CURRENT_USER_PHONE = "userphone";
    public static final String USER_COMAPNY_NAME = "usercompanyname";
    public static final String USER_ADDRESS = "useraddress";



    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_SIZE = "size";
    public static final String PRODUCT_SFT = "sft";
    public static final String PRODUCT_TYPE = "type";
    public static final String PRINTING_COST = "printing_cost";
    public static final String MOUNTING_COST = "mounting_cost";
    public static final String TOTAL_COST = "total_cost";
    public static final String PRODUCT_DESCRIPTION= "Description";
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_IMAGES = "images";
    public static final String STATE_ID = "state_id";
    public static final String CITY_ID= "city_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String PRODUCT_STATUS = "status";
    public static final String OFFER_TYPE = "offer_type";
    public static final String OFFER_QUANTITY = "offer";
    public static final String OFFER_STATUS = "offer_status";
    public static final String OFFER_NAME = "offer_name";
    public static final String OFFER_TOTAL = "offer_total";

    public static final String OFFER_IN_PERCENTAGE = "1";

    public static final String PRODUCT_SELL_STATUS = "product_sell_sts";
    public static final String INVOICE_URL = "invoice_url";

    public static final String PAGE_FROM = "from";
    public static final String PAGE_HOME = "home";
    public static final String PAGE_SEARCH = "search";
    public static final String PAGE_MENU = "menu";
    public static final String PAGE_PLACES = "places";
    public static final String PAGE_SERVICE = "service";
    public static final String PAGE_BEST_BANNERS = "best";
    public static final String PAGE_RECENT_BANNERS = "recent";

    public static final String PRODUCT_NOT_AVAILABLE = "1";
    public static final String PRODUCT_AVAILABLE = "0";
    public static final String RESULT_SUCCESS = "1";
    public static final String RESULT_FAILED = "0";

    public static final String PRODUCT_OFFER_AVAILABLE= "2";




    public static boolean isOnline(Context cntx) {
        ConnectivityManager cm =
                (ConnectivityManager) cntx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
