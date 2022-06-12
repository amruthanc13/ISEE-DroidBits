package com.droidbits.moneycontrol.ui.categories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.categories.CategoriesRepository;

import java.util.List;

public class CategoriesViewModel extends AndroidViewModel {
    private CategoriesRepository categoriesRepository;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        categoriesRepository = new CategoriesRepository(application);

    }

    /**
     * Insert a new category in the database.
     * @param categories category to be added.
     */
    public void insert(Categories categories) {
         categoriesRepository.insert(categories);
    }

    /**
     * Get all categories.
     * @return return all transactions from the database.
     */
    public List<Categories> getAllCategories() {
        return categoriesRepository.getAllCategories();
    }


    /**
     * Get all categories.
     * @return return all transactions from the database.
     */
    public String[] getCategoriesName() {
        return categoriesRepository.getCategoriesName();
    }

    /**
     * Get all categories.
     * @return return all transactions from the database.
     */
    public int[] getCategoriesIcon() {
        return categoriesRepository.getCategoriesIcon();
    }

    /**
     * get a  category.
     * @param categoryName category name.
     * @return category.
     */
    public Categories getSingleCategory(final String categoryName) {

        return categoriesRepository.getSingleCategory(categoryName);
    }

    /**
     * get a  category.
     * @param categoryName category name.
     * @return category.
     */
    public Categories getSingleCategory(final int categoryName) {

        return categoriesRepository.getSingleCategory(categoryName);
    }

}
