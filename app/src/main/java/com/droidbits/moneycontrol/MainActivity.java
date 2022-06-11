package com.droidbits.moneycontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.ui.budget.BudgetFragment;
import com.droidbits.moneycontrol.ui.categories.CategoriesFragment;
import com.droidbits.moneycontrol.ui.home.HomeFragment;
import com.droidbits.moneycontrol.ui.settings.SettingsFragment;
import com.droidbits.moneycontrol.ui.transactions.TransactionFragment;
import com.droidbits.moneycontrol.ui.users.SignInActivity;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Checking if the app is running for the 1st time
                boolean isFirstTime = sharedPreferencesUtils.getFirstTime();


                    Intent intent = new Intent(getApplication(), SignInActivity.class);
                    startActivity(intent);
                    finish();

            }
        }, TIMER);

        MoneyControlDB db = MoneyControlDB.getInstance(this);

    }

}