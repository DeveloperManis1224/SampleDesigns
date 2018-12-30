package com.app.adinn.outdoors.square_brace.adinn_outdoors.Services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HttpUtility {
    private HttpURLConnection httpConn;
    private DataOutputStream request;
    private String method = "POST";
    private String charset = "UTF-8";
    private final String boundary = "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    //	private BufferedWriter buffer;
    BufferedOutputStream bos;

    /**
     * This constructor initializes a new HTTP POST request with content type is
     * set to multipart/form-data
     *
     * @param requestURL
     * @throws IOException
     */
    public HttpUtility(String requestURL) throws IOException {

        // creates a unique boundary based on time stamp
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        httpConn.setRequestMethod(this.method);
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);
        httpConn.setRequestProperty("Accept", "application/json");

        //httpConn.setRequestProperty("content-Type", "application/json");

        request = new DataOutputStream(httpConn.getOutputStream());
        bos = new BufferedOutputStream(httpConn.getOutputStream());
    }


    public HttpUtility(String requestURL, ArrayList<KeyValue> params) throws IOException {

        this.method = "GET";
        // creates a unique boundary based on time stamp
        String query = "";
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                KeyValue param = params.get(i);
                if (i != 0) {
                    query += "&";
                }
                query += URLEncoder.encode(param.key, this.charset) + "="
                        + URLEncoder.encode(param.value, this.charset);
            }
        }
        URL url = new URL(requestURL + "?" + query);

        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod(this.method);
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws IOException {
        bos.write(new String(this.twoHyphens + this.boundary + this.crlf).getBytes());
        bos.write(new String("Content-Disposition: form-data; name=\"" + name + "\"" + this.crlf).getBytes());
        bos.write(new String("Content-Type: text/plain; charset=UTF-8" + this.crlf).getBytes());
        bos.write(new String(this.crlf).getBytes());
        bos.write(new String(value).getBytes());
        bos.write(this.crlf.getBytes());
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        byte[] bytes = read(uploadFile);
        bos.write(new String(this.twoHyphens + this.boundary + this.crlf).getBytes());
        bos.write(new String("Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + this.crlf).getBytes());
        bos.write(new String(this.crlf).getBytes());
        bos.write(bytes);
    }

    public byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned status
     * OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        String response = "";
        if (this.method.equals("POST")) {
            /*request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
			request.flush();
			request.close();*/
//            bos.write(this.crlf.getBytes());
            bos.write((this.twoHyphens + this.boundary + this.twoHyphens + this.crlf).getBytes());
            //buffer.flush();
            bos.close();
        }
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            httpConn.disconnect();
        } else {
            InputStream responseStream = new BufferedInputStream(httpConn.getErrorStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String message = stringBuilder.toString();
            httpConn.disconnect();
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }
}