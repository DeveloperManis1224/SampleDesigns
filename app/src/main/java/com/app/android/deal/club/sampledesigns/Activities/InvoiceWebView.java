package com.app.android.deal.club.sampledesigns.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;

public class InvoiceWebView extends AppCompatActivity {

    WebView webInvoice ;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webInvoice = findViewById(R.id.web_view_invoice);
        String inovice_url = getIntent().getExtras().getString(Constants.INVOICE_URL);
        webInvoice.setWebViewClient(new WebViewClient());
        webInvoice.getSettings().setLoadsImagesAutomatically(true);
        webInvoice.getSettings().setJavaScriptEnabled(true);
        webInvoice.getSettings().setLoadWithOverviewMode(true);
        webInvoice.getSettings().setUseWideViewPort(true);
        webInvoice.getSettings().setSupportZoom(true);
        webInvoice.setVerticalScrollBarEnabled(false);
        webInvoice.setHorizontalScrollBarEnabled(false);
        webInvoice.loadUrl(inovice_url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
