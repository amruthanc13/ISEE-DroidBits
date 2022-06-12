package com.droidbits.moneycontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.ui.users.SignInActivity;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {
    private SharedPreferencesUtils sharedPreferencesUtils;
    private static final int TIMER = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

        setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplication(), SignInActivity.class);
        startActivity(intent);
        finish();

        MoneyControlDB db = MoneyControlDB.getInstance(this);

    }

}