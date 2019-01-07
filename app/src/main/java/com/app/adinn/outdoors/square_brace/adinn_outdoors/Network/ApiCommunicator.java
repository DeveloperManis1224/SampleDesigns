package com.app.adinn.outdoors.square_brace.adinn_outdoors.Network;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.app.android.deal.dealclub.Data.LoginData;
import com.app.android.deal.dealclub.Data.LoginResponse;
import com.app.android.deal.dealclub.Data.RegistrationResponse;
import com.app.android.deal.dealclub.Data.UserData;
import com.app.android.deal.dealclub.Data.UserId;
import com.app.android.deal.dealclub.HomePage;
import com.app.android.deal.dealclub.LoginPage;
import com.app.android.deal.dealclub.UpdatProfile;
import com.app.android.deal.dealclub.Util.Constants;
import com.app.android.deal.dealclub.Util.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCommunicator {
    private static final String TAG = "communicator";
  //  private static final String SERVER_URL = "http://192.168.1.112:8080";
    private static final String SERVER_URL = "http://1.23.183.201:1808";
    private Context context;
    SessionManager session = new SessionManager();

    public ApiCommunicator(Context context) {
        this.context = context;

    }

    public void Login_Post(String username, String password) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).build();
        final APIService service = retrofit.create(APIService.class);
        LoginData loginData  =new LoginData(username,password);
        Call<LoginResponse> call = service.postLogin(loginData);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse mLoginObject = response.body();
                String message = mLoginObject.getMessage();
                String status = mLoginObject.getStatus();
                String user_Id = mLoginObject.getId();
                String phone = mLoginObject.getContact();
                String email = mLoginObject.getEmail();
                String dob = mLoginObject.getDob();
                String name  = mLoginObject.getName();

                Log.v("PESPONSE_LOGIN",status+"///"+message+"///"+user_Id);

                Log.v("message",message);
                Log.v("status",status);
                if(status.equalsIgnoreCase(Constants.RESPONSE_SUCCESS))
                {
                    Toast.makeText(context, "Login Successsfull", Toast.LENGTH_SHORT).show();
                    session.setPreferences(context,Constants.CURRENT_USER_ID,user_Id);
                    session.setPreferences(context, Constants.LOGIN_STATUS,Constants.LOGIN);
                    session.setPreferences(context,Constants.PROFILE_COMPLETE,Constants.INCOMPLETE);
                    session.setPreferences(context,Constants.CURRENT_USER_DOB,dob);
                    session.setPreferences(context,Constants.CURRENT_USER_EMAIL,email);
                    session.setPreferences(context,Constants.CURRENT_USER_NAME,name);
                    session.setPreferences(context,Constants.CURRENT_USER_PHONE,phone);
                    Intent intent = new Intent(context, HomePage.class);
                    context.startActivity(intent);
                }
                else if(status.equalsIgnoreCase(Constants.RESPONSE_FAILED))
                {
                    Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Connection Problem : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfile(String id,String name, final String contact, String email,
                              String dob, String gender, String password, String anniversary) {
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).build();
        APIService service = retrofit.create(APIService.class);
        UserData userData = new UserData(id,name, gender, contact, dob, email, password,anniversary);
        Call<RegistrationResponse> call = service.updateUser(userData);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
            RegistrationResponse registrationResponse = response.body();
            if(response.isSuccessful())
            {
                Log.e("RESPONSE_UPDATE",""+registrationResponse.getMessage()+"/"+registrationResponse.getStatus());
                if(registrationResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS))
                {
                    Toast.makeText(context, registrationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context,HomePage.class));
                    session.setPreferences(context,Constants.PROFILE_COMPLETE,Constants.COMPLETE);
                }
                else
                {
                    Toast.makeText(context, registrationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            }
            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

            }
        });
    }

    public void Register_Post(String name, final String contact, String email, String dob,
                              String gender, String password,String anniversary) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).build();
        APIService service = retrofit.create(APIService.class);
        UserData userData = new UserData(name, gender, contact, dob, email, password, anniversary);
        Call<RegistrationResponse> call = service.createUser(userData);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if(response.isSuccessful())
                {
                    String message = registrationResponse.getMessage();
                    String status = registrationResponse.getStatus();
                    String userId = registrationResponse.getuId();
                    Log.v("message",message);
                    Log.v("status",status);
                    if(status.equalsIgnoreCase(Constants.RESPONSE_SUCCESS))
                    {
                        Toast.makeText(context, "Register Successsfully", Toast.LENGTH_SHORT).show();
                        session.setPreferences(context,Constants.CURRENT_USER_ID,userId);
                        session.setPreferences(context, Constants.LOGIN_STATUS,Constants.LOGIN);
                        session.setPreferences(context,Constants.PROFILE_COMPLETE,Constants.INCOMPLETE);
                        Intent intent = new Intent(context, LoginPage.class);
                        context.startActivity(intent);
                    }
                    else if(status.equalsIgnoreCase(Constants.RESPONSE_FAILED))
                    {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(context, "Exception : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getProfileStatus_POST(String user_id) {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).build();
        APIService service = retrofit.create(APIService.class);
        UserId userData = new UserId(user_id);
        Call<LoginResponse> call =  service.getProfileCompleteStatus(userData);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                Log.v("RESPONSE_LOGIN", "///"+response.body()+"///");
//                Log.v("RESPONSE_LOGIN",loginResponse.getMessage()+"///"+loginResponse.getStatus()+"///");
                if(response.isSuccessful())
                {
                    if(loginResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS))
                    {
                        if(loginResponse.getMessage().equalsIgnoreCase(Constants.INCOMPLETE))
                        {
                            Log.e("Sts","true");
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setTitle("Alert");
                            builder1.setMessage("Please complete your profile and get more benefits");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton(
                                    "Update now",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            context.startActivity(new Intent(context, UpdatProfile.class));
                                        }
                                    });
                            builder1.setNegativeButton(
                                    "Skip",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            session.setPreferences(context,Constants.PROFILE_COMPLETE,Constants.INCOMPLETE);
                                        }
                                    });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                        else
                        {
                            Log.e("Sts","false");
                        }
                    }
                    else
                    {
                        Toast.makeText(context, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    session.setPreferences(context,Constants.PROFILE_COMPLETE, Constants.COMPLETE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

 }

