package com.app.android.deal.club.sampledesigns.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.bumptech.glide.Glide;

public class ProductDetails extends AppCompatActivity {

    String productId,productName,productType,productSFT,productSize,printingCost,mountingCost,totalCost,
    productDescription, productImage,stateId,cityId,categoryId,productSts;
    private TextView mProductName,mProductType,mProductSft,mProductSize,mPrintingCost,mMountingCost,
    mTotalCost, mDescription, mProductStatus;
    private ImageView mProductImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
    }

    private void init()
    {
        mProductName = findViewById(R.id.d_txt_product_name);
        mProductType = findViewById(R.id.d_txt_type);
        mProductSft = findViewById(R.id.d_txt_sft);
        mProductSize = findViewById(R.id.d_txt_size);
        mPrintingCost = findViewById(R.id.d_txt_printting_cost);
        mMountingCost = findViewById(R.id.d_txt_mounting_cost);
        mTotalCost = findViewById(R.id.d_total_cost);
        mDescription = findViewById(R.id.d_txt_description);
        mProductStatus = findViewById(R.id.d_dis_cost);
        mProductImage = findViewById(R.id.produ_img);
        productId = getIntent().getExtras().getString(Constants.PRODUCT_ID);
        productName = getIntent().getExtras().getString(Constants.PRODUCT_NAME);
        productType = getIntent().getExtras().getString(Constants.PRODUCT_TYPE);
        productSFT = getIntent().getExtras().getString(Constants.PRODUCT_SFT);
        productSize = getIntent().getExtras().getString(Constants.PRODUCT_SIZE);
        printingCost = getIntent().getExtras().getString(Constants.PRINTING_COST);
        mountingCost = getIntent().getExtras().getString(Constants.MOUNTING_COST);
        totalCost = getIntent().getExtras().getString(Constants.TOTAL_COST);
        productDescription = getIntent().getExtras().getString(Constants.PRODUCT_DESCRIPTION);
        productImage = getIntent().getExtras().getString(Constants.PRODUCT_IMAGE);
        stateId = getIntent().getExtras().getString(Constants.STATE_ID);
        cityId = getIntent().getExtras().getString(Constants.CITY_ID);
        categoryId = getIntent().getExtras().getString(Constants.CATEGORY_ID);
        productSts = getIntent().getExtras().getString(Constants.PRODUCT_STATUS);

        mProductName.setText(getString(R.string.filled_bullet) +" Name            :"+productName);
        mProductType.setText(getString(R.string.filled_bullet) +" Type            :"+productType);
        mProductSft.setText(getString(R.string.filled_bullet) +" Sft             :"+productSFT);
        mProductSize.setText(getString(R.string.filled_bullet) +" Size            :"+productSize);
        mPrintingCost.setText(getString(R.string.filled_bullet) +" Printing Cost   :"+printingCost);
        mMountingCost.setText(getString(R.string.filled_bullet) +" Mounting Cost   :"+mountingCost);
        mTotalCost.setText(getString(R.string.filled_bullet) +" Total Cost      :"+totalCost);
        mDescription.setText(getString(R.string.filled_bullet) +" Description     :"+productDescription);
        mProductStatus.setText(getString(R.string.filled_bullet) +" Status          :"+productSts);

        Glide.with(ProductDetails.this).
                load(Constants.APP_BASE_URL+productImage).into(mProductImage);


    }
}
