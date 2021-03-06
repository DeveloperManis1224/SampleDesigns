package com.app.adinn.outdoors.square_brace.adinn_outdoors;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities.HomeActivity;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities.LoginPage;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Firebase.MyFirebaseInstanceIDService;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        if (Constants.isOnline(SplashScreen.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                    if (!prefs.getBoolean("firstTime", false)) {
                        DBHelper db = new DBHelper(SplashScreen.this);
                        startActivity(new Intent(SplashScreen.this, LoginPage.class));
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.commit();
                    } else {
                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    }

                }
            }, 3000);
        } else {
            new AwesomeSuccessDialog(SplashScreen.this)
                    .setTitle("No Internet Available!")
                    .setMessage("There is no internet connection.please turn on your internet connection.")
                    .setColoredCircle(R.color.colorAccent)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("Refresh")
                    .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            Intent in = new Intent(SplashScreen.this, SplashScreen.class);
                            startActivity(in);
                            finish();
                        }
                    })
                    .show();
        }
    }

}
