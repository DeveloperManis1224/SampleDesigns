package com.app.adinn.outdoors.square_brace.adinn_outdoors.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Opus on 03/03/2017
 */
public class HttpTask extends AsyncTask<String, String, String> {
    private ArrayList<KeyValue> params;
    private HashMap<String, File> files;
    private Context context;
    private String requestURL;
    private String token,waitmsg;
    private CallBack callback;
    private boolean getmethod;
    private ProgressDialog progressdialog;


    public HttpTask(Context context, ArrayList<KeyValue> params, HashMap<String, File> files, String requestURL, CallBack callback, boolean getmethod) {
        this.params = params;
        this.files = files;
        this.context = context;
        this.requestURL = requestURL;
        this.callback = callback;
        this.getmethod = getmethod;
        this.waitmsg="Please Wait";
    }

    @Override
    protected void onPreExecute() {

        this.progressdialog = new ProgressDialog(this.context);
        this.progressdialog.setCancelable(false);
        this.progressdialog.setIndeterminate(true);
        this.progressdialog.setMessage(waitmsg);
        if(callback.isVisible()) {
            this.progressdialog.show();
        }
    }

    protected String doInBackground(String... arg0) {
        String response = "";
        try {
            if(getmethod) {
                HttpUtilityAuthorization multipart = new HttpUtilityAuthorization(requestURL,params);
                response = multipart.finish();
            }else{
                String charset = "UTF-8";
                HttpUtilityAuthorization multipart = new HttpUtilityAuthorization(requestURL);
                for (KeyValue temp : params) {
                    String key = temp.key.trim();
                    String value = temp.value.trim();
                    multipart.addFormField(key, value);
                    Log.d("values", key + ":" + value);
                }
                Iterator i;
                if (files != null) {
                    i = files.keySet().iterator();
                    while (i.hasNext()) {
                        String key = (String) i.next();
                        File value = this.files.get(key);
                        multipart.addFilePart(key, value);
                    }
                }
                response = multipart.finish();
            }
        } catch (Exception e) {
            Log.d("response", new String("Exception: " + e.toString()));
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if (this.progressdialog != null && callback.isVisible()) {
            this.progressdialog.dismiss();        }
        this.callback.onPostExecute(result);
    }

    public String getPostDataString(HashMap<String, String> params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keyset = params.keySet();
        Iterator<String> itr = keyset.iterator();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

}

