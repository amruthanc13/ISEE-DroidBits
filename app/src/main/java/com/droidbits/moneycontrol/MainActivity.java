package com.droidbits.moneycontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.droidbits.moneycontrol.ui.budget.BudgetFragment;
import com.droidbits.moneycontrol.ui.categories.CategoriesFragment;
import com.droidbits.moneycontrol.ui.home.HomeFragment;
import com.droidbits.moneycontrol.ui.settings.SettingsFragment;
import com.droidbits.moneycontrol.ui.transactions.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

   private BottomNavigationView.OnNavigationItemSelectedListener navListener =
           new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.transactions:
                            selectedFragment = new TransactionFragment();
                            break;
                        case R.id.categories:
                            selectedFragment = new CategoriesFragment();
                            break;
                        case R.id.budget:
                            selectedFragment = new BudgetFragment();
                            break;
                        case R.id.settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
       }
   };
}