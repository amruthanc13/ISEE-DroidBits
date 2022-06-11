package com.droidbits.moneycontrol.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidbits.moneycontrol.R;

public final class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static final String CURRENT_USER_ID_KEY = "currentUserId";
    private static final String IS_SIGNED_IN_KEY = "isUserSignedIn";
    private static final String FIRST_TIME_SET = "isApplicationFirstTime2";
    private static final String FIRST_TIME = "isApplicationFirstTime1";

    /**
     * Constructor.
     * @param application application.
     */
    public SharedPreferencesUtils(final Application application) {
        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.shared_preferences_file_key),
                Context.MODE_PRIVATE
        );
    }

    /**
     * Set current user id.
     * @param userId user id.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setCurrentUserId(final String userId) {
        sharedPreferences.edit().putString(CURRENT_USER_ID_KEY, userId).commit();

        return this;
    }

    /**
     * Get current user id.
     * @return id.
     */
    public String getCurrentUserId() {
        return sharedPreferences.getString(CURRENT_USER_ID_KEY, "");
    }


    /**
     * Set user as signed in or not.
     * @param isSignedIn bool.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setIsSignedIn(final boolean isSignedIn) {
        sharedPreferences.edit().putBoolean(IS_SIGNED_IN_KEY, isSignedIn).commit();

        return this;
    }

    /**
     * Get whether user is signed in or not.
     * @return boolean.
     */
    public boolean getIsSignedIn() {
        return sharedPreferences.getBoolean(IS_SIGNED_IN_KEY, false);
    }

    /**
     * Set boolean for checking if its 1st time.
     * @param firstTime user id.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setFirstTimeSet(final Boolean firstTime) {
        sharedPreferences.edit().putBoolean(FIRST_TIME_SET, firstTime).commit();

        return this;
    }

    /**
     * Get if application is running for the 1st time.
     * @return boolean.
     */
    public Boolean getFirstTimeSet() {
        return sharedPreferences.getBoolean(FIRST_TIME_SET, true);
    }

    /**
     * Set boolean for checking if its 1st time.
     * @param firstTime user id.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setFirstTime(final Boolean firstTime) {
        sharedPreferences.edit().putBoolean(FIRST_TIME, firstTime).commit();

        return this;
    }

    /**
     * Get if application is running for the 1st time.
     * @return boolean.
     */
    public Boolean getFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME, true);
    }
}
