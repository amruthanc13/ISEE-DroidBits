package com.droidbits.moneycontrol.db.categories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface CategoriesDao {

    @Insert
    long insert(Categories categories);

    @Query("DELETE from categories WHERE id=:categoriesId")
    void deleteCategories(
            int categoriesId
    );

    @Query("DELETE FROM categories")
    void deleteAllCategories();

    @Query("SELECT * FROM categories ")
    LiveData<List<Categories>> getAllCategories();

    @Query("SELECT * FROM categories WHERE id=:categoriesId")
    Categories getCategoriesById(long categoriesId);

}
