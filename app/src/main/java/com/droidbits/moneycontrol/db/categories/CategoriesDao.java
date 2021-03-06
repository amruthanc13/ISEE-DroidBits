package com.droidbits.moneycontrol.db.categories;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoriesDao {

    // TODO: move to @Query decorator to allow for default values;
    /**
     * Insert a new category into the database.
     * @param category category to be saved.
     * @return category ID
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Categories category);

    /**
     * Delete all categories from the database.
     */
    @Query("DELETE FROM Categories")
    void deleteAll();

    /**
     * Get single category.
     * @param id category ID.
     * @return Single category matching the ID
     */
    @Query("SELECT * FROM categories WHERE id=:id AND user_id=:userId AND account=:accountId")
    Categories getSingleCategory(int id, String userId, String accountId);

    /**
     * Get single category.
     * @param name category name.
     * @return Single category matching with name
     */
    @Query("SELECT * FROM categories WHERE name=:name AND user_id=:userId AND account=:accountId ")
    Categories getSingleCategory(String name, String userId, String accountId);

    /**
     * Get all categories from the database.
     * @param userId
     * @return list of categories
     */
    @Query("SELECT * FROM Categories where name != 'Income' AND  user_id=:userId AND account=:accountId ")
    List<Categories> getAllCategories(String userId, String accountId);


    /**
     * Get all categories name from the database.
     * @param userId
     * @param income
     * @return string of categories
     */
    @Query("SELECT name FROM Categories WHERE "
            +
            " name != :income"
            +
            " AND  user_id=:userId" +
            " AND account=:accountId")
    String[] getCategoriesName(String userId, String income, String accountId);


    /**
     * Get all categories name from the database.
     * @param income income category.
     * @return list of categories name.
     */
    @Query("SELECT icon FROM Categories WHERE " +
            " user_id=:userId "+
            "AND  account=:accountId "+
            "AND  name != :income")
    int[] getCategoriesIcon(String income, String userId , String accountId );

}
