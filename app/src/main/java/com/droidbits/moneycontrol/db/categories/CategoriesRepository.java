package com.droidbits.moneycontrol.db.categories;

import android.app.Application;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class CategoriesRepository {

    private CategoriesDao categoriesDao;


    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public CategoriesRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        categoriesDao = db.categoriesDao();
    }

    /**
     * Get all the saved categories.
     * @return list of saved categories.
     */
    public List<Categories> getAllCategories() {

        return categoriesDao.getAllCategories();
    }


    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Categories category) {
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
        String income = "Income";


        return categoriesDao.getCategoriesName(income);
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
