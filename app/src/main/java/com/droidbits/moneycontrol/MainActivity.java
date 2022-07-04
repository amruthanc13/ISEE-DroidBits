package com.droidbits.moneycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.ui.intro.AppIntro;
import com.droidbits.moneycontrol.ui.users.SignInActivity;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {
    private SharedPreferencesUtils sharedPreferencesUtils;
    private static final int TIMER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

     /*   setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplication(), SignInActivity.class);
        startActivity(intent);
        finish();*/

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/
                //Checking if the app is running for the 1st time
                boolean isFirstTime = sharedPreferencesUtils.getFirstTime();

                if (isFirstTime) {
                    sharedPreferencesUtils.setFirstTime(false);

                    Intent intent = new Intent(getApplication(), AppIntro.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplication(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                }

     //   }, 1);

        MoneyControlDB db = MoneyControlDB.getInstance(this);

    }

}