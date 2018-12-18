package com.app.android.deal.club.sampledesigns.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;

public class InvoiceWebView extends AppCompatActivity {

    WebView webInvoice ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_web_view);
        webInvoice = findViewById(R.id.web_view_invoice);
        String inovice_url = getIntent().getExtras().getString(Constants.INVOICE_URL);
        webInvoice.getSettings().setLoadsImagesAutomatically(true);
        webInvoice.getSettings().setJavaScriptEnabled(true);
        webInvoice.loadUrl(inovice_url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
