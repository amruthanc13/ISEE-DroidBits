package com.droidbits.moneycontrol.db.budget;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.db.categories.Categories;

import java.util.List;

public class BudgetRepository {
    private BudgetDao budgetDao;

    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public BudgetRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        budgetDao = db.budgetDao();
    }

    /**
     * Get all the saved Budget.
     * @return list of saved categories.
     */
    public LiveData<List<Budget>> getAllBudget() {

        return budgetDao.getAllBudget();
    }


    /**
     * Insert a new Budget.
     * @param budget category to be saved.
     */
    public void insert(final Budget budget) {
        budgetDao.insert(budget);

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
        return budgetDao.getBudgetWithCategory(category);
    }



}
