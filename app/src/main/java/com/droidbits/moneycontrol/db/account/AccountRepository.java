package com.droidbits.moneycontrol.db.account;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;


import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class AccountRepository {

    private AccountDao accountDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Constructor.
     * @param application application.
     */
    public AccountRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        accountDao = db.accountDao();
    }

    /**
     * Get all projects from the database.
     * @return all projects in the database.
     */
    public LiveData<List<Account>> getAllAccounts() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return accountDao.getAllAccounts(currentUserId);
    }

    /**
     * Get accounts for export csv.
     * @return list of user accounts.
     */
    public List<Account> getAccountsForExport() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return accountDao.getAccountsForExport(currentUserId);
    }

    /**
     * Get account by id.
     * @param accountId id.
     * @return account.
     */
    public Account getAccountById(final int accountId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        Log.v("USER", currentUserId);

        if (currentUserId.equals("")) {
            return null;
        }

        Account account = accountDao.getAccountById(accountId, currentUserId);

        return account;
    };

    /**
     * Get account by id.
     * @param accountName id.
     * @return transaction.
     */
    public Account getAccountByName(final String accountName) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        Account account = accountDao.getAccountByName(accountName, currentUserId);

        return account;
    };

    /**
     * get account names.
     * @return the account names
     */
    public String[] getAccountNames() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return accountDao.getAccountNames(currentUserId);
    }

    /**
     * Insert new account to the selected user.
     * @param account account to be saved.
     * @return account id.
     */
    public long insert(final Account account) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return -1;
        }

        account.setOwnerId(currentUserId);

        return accountDao.insert(account);
    }
}
