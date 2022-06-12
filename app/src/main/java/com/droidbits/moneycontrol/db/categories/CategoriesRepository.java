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

        if (currentUserId.equals("")) {
            return null;
        }

        return categoriesDao.getAllCategories(currentUserId);
    }


    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Categories category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }
        category.setUserId(currentUserId);

        categoriesDao.insert(category);
    }


    /**
     * get a  category.
     * @param categoryId category id.
     * @return Category.
     */
    public Categories getSingleCategory(final int categoryId) {
        return categoriesDao.getSingleCategory(categoryId);
    }

    /**
     * get a category.
     * @param categoryName category name.
     * @return category.
     */
    public Categories getSingleCategory(final String categoryName) {
        return categoriesDao.getSingleCategory(categoryName);
    }
    /**
     * get a category.
     * @return the category names
     */
    public String[] getCategoriesName() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String income = "Income";

        if (currentUserId.equals("")) {
            return null;
        }

        return categoriesDao.getCategoriesName(currentUserId, income);
    }

    /**
     * get a category.
     * @return the category names
     */
    public int[] getCategoriesIcon() {
        String income = "Income";


        return categoriesDao.getCategoriesIcon(income);
    }

}
