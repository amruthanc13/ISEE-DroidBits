package com.droidbits.moneycontrol.db.categories;

import android.app.Application;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class CategoriesRepository {

    private CategoriesDao categoriesDao;
    private SharedPreferencesUtils sharedPreferencesUtils;


    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public CategoriesRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        categoriesDao = db.categoriesDao();
    }

    /**
     * Get all the saved categories.
     * @return list of saved categories.
     */
    public List<Categories> getAllCategories() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoriesDao.getAllCategories(currentUserId, currentAccountId);
    }


    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Categories category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }
        category.setUserId(currentUserId);
        category.setAccount(currentAccountId);

        categoriesDao.insert(category);
    }


    /**
     * get a  category.
     * @param categoryId category id.
     * @return Category.
     */
    public Categories getSingleCategory(final int categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoriesDao.getSingleCategory(categoryId, currentUserId, currentAccountId);
    }

    /**
     * get a category.
     * @param categoryName category name.
     * @return category.
     */
    public Categories getSingleCategory(final String categoryName) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }
        return categoriesDao.getSingleCategory(categoryName, currentUserId, currentAccountId);
    }
    /**
     * get a category.
     * @return the category names
     */
    public String[] getCategoriesName() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        String income = "Income";

        if (currentUserId.equals("")) {
            return null;
        }

        return categoriesDao.getCategoriesName(currentUserId, income, currentAccountId);
    }

    /**
     * get a category.
     * @return the category names
     */
    public int[] getCategoriesIcon() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        String income = "Income";

        if (currentUserId.equals("")) {
            return null;
        }


        return categoriesDao.getCategoriesIcon(income, currentUserId, currentAccountId);
    }

}
