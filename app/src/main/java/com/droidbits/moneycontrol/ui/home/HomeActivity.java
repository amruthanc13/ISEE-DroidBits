package com.droidbits.moneycontrol.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.ui.budget.BudgetFragment;
import com.droidbits.moneycontrol.ui.categories.CategoriesFragment;
import com.droidbits.moneycontrol.ui.settings.SettingsFragment;
import com.droidbits.moneycontrol.ui.transactions.TransactionFragment;
import com.droidbits.moneycontrol.ui.users.SignInActivity;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private SharedPreferencesUtils sharedPreferencesUtils;
    /**
     * Create home activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, new HomeFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
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
                        default:
                            selectedFragment = new HomeFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    /**
     * If user is not signed in, request token.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!sharedPreferencesUtils.getIsSignedIn()) {
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Sign user out whenever he / she leaves the application.
     */
    @Override
    protected void onStop() {
        super.onStop();

        sharedPreferencesUtils.setIsSignedIn(false);
    }
}
