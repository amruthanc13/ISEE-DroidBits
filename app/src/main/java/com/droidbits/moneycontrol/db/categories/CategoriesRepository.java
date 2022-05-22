package com.droidbits.moneycontrol.db.categories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class CategoriesRepository {

    private CategoriesDao categoriesDao;
    private LiveData<List<Categories>> allCategories;

    public CategoriesRepository(Application application){
        MoneyControlDB database = MoneyControlDB.getInstance(application);
        categoriesDao = database.categoriesDao();
    }

    public LiveData<List<Categories>> getAllCategories() {

        return categoriesDao.getAllCategories();
    }

    public Categories getCategoriesById(final long categoriesId) {

        return categoriesDao.getCategoriesById(categoriesId);
    };

    public void deleteCategories(final int categoriesId) {

        categoriesDao.deleteCategories(
                categoriesId
        );
    }

    public Long insert(final Categories categories) {
        return categoriesDao.insert(categories);
    }

    public void deleteAllCategories() {

        categoriesDao.deleteAllCategories();
    }

}
