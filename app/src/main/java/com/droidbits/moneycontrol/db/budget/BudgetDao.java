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

    @Query("SELECT SUM(amount) FROM budget WHERE categories =:categoryId AND user_id=:userId AND account=:accountId")
    double getBudgetAmountByCategory(String categoryId, String userId, String accountId);
    
    /**
     * Get all budget from the database.
     * @param userID
     * @return list of budget
     */
    @Query("SELECT * FROM budget where user_id =:userID AND account=:accountId")
    LiveData<List<Budget>> getAllBudget(String userID, String accountId);

    /**
     * Get budget from the database.
     * @param categoryID
     * @param userID
     * @return budget object
     */
    @Query("SELECT * FROM budget where categories=:categoryID and user_id =:userID AND account=:accountId")
    Budget getBudgetWithCategory(String categoryID, String userID, String accountId);

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

    /**
     * Delete budget.
     * @param budgetId budget id.
     * @param ownerId owner id.
     * @param accountId account id.
     */
    @Query("DELETE FROM budget WHERE id=:budgetId AND user_id=:ownerId AND "
            + "account=:accountId")
    void deleteBudget(int budgetId, String ownerId, String accountId);

}
