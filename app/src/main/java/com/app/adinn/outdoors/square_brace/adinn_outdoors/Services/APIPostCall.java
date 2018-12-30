package com.app.adinn.outdoors.square_brace.adinn_outdoors.Services;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class APIPostCall extends AsyncTask<String, String, String> {
    private String url;
    private APICallback callback;
    private ArrayList<KeyValue> params;

    public APIPostCall(String url, APICallback callback, ArrayList<KeyValue> params) {
        this.url = url;
        this.callback = callback;
        this.params = params;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String contentAsString = null;
        try {
            HttpUtility utility = new HttpUtility(this.url);
            for (KeyValue temp : params) {
                utility.addFormField(temp.key, temp.value);
            }
            contentAsString = utility.finish();
        } catch (Exception e)
        {
            Log.e("ERROR", "" + e.getMessage());
        }
        return contentAsString;

    }

    private static String readIt(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.callback.onPostExecute(s);
    }
}
