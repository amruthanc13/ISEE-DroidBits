package com.droidbits.moneycontrol.db.budget;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.db.users.UsersDao;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class BudgetRepository {
    private BudgetDao budgetDao;
    private UsersDao userDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public BudgetRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        budgetDao = db.budgetDao();
    }

    /**
     * Get all the saved Budget.
     * @return list of saved categories.
     */
    public LiveData<List<Budget>> getAllBudget() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();


        if (currentUserId.equals("")) {
            return null;
        }

        return budgetDao.getAllBudget(currentUserId, currentAccountId);
    }


    /**
     * Insert a new Budget.
     * @param budget category to be saved.
     */
    public void insert(final Budget budget) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }
        budget.setUserId(currentUserId);
        budget.setAccount(currentAccountId);

        budgetDao.insert(budget);

    }
    public double getBudgetAmountByCategory(String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return 0;
        }
        return budgetDao.getBudgetAmountByCategory(categoryId, currentUserId, currentAccountId);
    }

    /**
     * get a  budget.
     * @param id category id.
     * @return Category.
     */
    public Budget getSingleBudget(final int id) {

        return budgetDao.getSingleBudget(id);
    }


    /**
     * get a category.
     * @param category category id.
     * @return category.
     */
    public Budget getBudgetWithCategory(final String category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();


        if (currentUserId.equals("")) {
            return null;
        }
        return budgetDao.getBudgetWithCategory(category, currentUserId, currentAccountId);
    }

    /**
     * Convert budgets.
     * @param conversionRate conversion rate.
     */
    public void updateBudgetAmountsDefaultCurrency(final float conversionRate) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        budgetDao.updateBudgetAmountsDefaultCurrency(conversionRate, currentUserId);
    }

    public void deleteBudget(final int budgetId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }

        budgetDao.deleteBudget(budgetId, currentUserId, currentAccountId);
    }



}
