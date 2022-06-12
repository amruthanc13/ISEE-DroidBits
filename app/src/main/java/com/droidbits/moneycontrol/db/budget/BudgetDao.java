package com.droidbits.moneycontrol.db.budget;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetDao {
    // TODO: move to @Query decorator to allow for default values;
    /**
     * Insert a new category into the database.
     * @param budget category to be saved.
     * @return budget ID
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Budget budget);

    /**
     * Delete all budget from the database.
     */
    @Query("DELETE FROM budget")
    void deleteAll();

    /**
     * Get single budget.
     * @param id category ID.
     * @return Single category matching the ID
     */
    @Query("SELECT * FROM budget WHERE id=:id")
    Budget getSingleBudget(int id);


    /**
     * Get all budget from the database.
     */
    @Query("SELECT * FROM budget where user_id =:userID")
    LiveData<List<Budget>> getAllBudget(String userID);

    /**
     * Get budget from the database.
     */
    @Query("SELECT * FROM budget where categories=:categoryID and user_id =:userID")
    Budget getBudgetWithCategory(String categoryID, String userID);

    /**
     * Update budget amounts default currency.
     * @param conversionRate conversion rate.
     * @param userId owner id.
     */
    @Query("UPDATE budget SET "
            + "amount=:conversionRate * amount "
            + "WHERE user_id=:userId;"
    )
    void updateBudgetAmountsDefaultCurrency(float conversionRate, String userId);

}
