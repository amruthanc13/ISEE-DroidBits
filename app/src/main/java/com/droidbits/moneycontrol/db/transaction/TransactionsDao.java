package com.droidbits.moneycontrol.db.transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert
    long insert(Transactions transactions);

    @Query("DELETE from transactions WHERE id=:transactionId")
    void deleteTransaction(
            int transactionId
    );

    @Query("DELETE FROM transactions")
    void deleteAllTransactions();

    /**
     * Retrieve all transactions from the database.
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE user_id=:userId "
            + "ORDER BY date DESC")
    LiveData<List<Transactions>> getAllTransactions(String userId);

    //Get sum of expenses
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Expense' AND user_id=:userId")
    double getExpenseTransactionSum(String userId);

    //Get sum of income
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Income' AND user_id=:userId")
    double getIncomeTransactionSum(String userId);

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */

    @Query("SELECT * FROM transactions WHERE id=:transactionId AND user_id=:userId ")
    Transactions getTransactionById(long transactionId, String userId);


    /**
     * Retrieve Filtered transactions from the database.
     * @return Filtered transactions.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE amount>=:amountFrom "
            + "AND  amount<=:amountTo "
            + "AND (date BETWEEN :dateFrom AND :dateTo) "
            + "AND (:paymentMethod IS NULL OR method =:paymentMethod) "
            + "AND (:categoryId IS NULL OR categories =:categoryId) "
            + "AND user_id =:userId "
            + "ORDER BY date DESC")
    List<Transactions> filterTransactions(
            Float amountFrom,
            Float amountTo,
            Long dateFrom,
            Long dateTo,
            String paymentMethod,
            String categoryId,
            String userId);

    /**
     * Update transaction recurring params.
     * @param transactionId id.
     * @param isRepeating flag.
     * @param repeatingIntervalType type.
     */
    @Query("UPDATE transactions SET "
            + "is_repeating=:isRepeating, "
            + "repeating_interval_type=:repeatingIntervalType "
            + "WHERE id=:transactionId ")
    void updateTransactionRepeatingFields(
            int transactionId,
            Boolean isRepeating,
            Integer repeatingIntervalType
    );

    /**
     * Update transaction amounts default currency.
     * @param conversionRate conversion rate.
     * @param userId user id.
     */
    @Query("UPDATE transactions SET "
            + "amount=:conversionRate * amount "
            + "WHERE user_id=:userId;"
    )
    void updateTransactionAmountsDefaultCurrency(float conversionRate, String userId);


    /**
     * Retrieve sum of transaction income amount for a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @return sum of transaction income amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE categories=:categoryId "
            + "AND user_id=:ownerId "
            + "AND type =:income")
    float getTotalIncomeByCategoryId(String categoryId, String ownerId, String income);

    /**
     * Retrieve sum of transaction expense amount for a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @return sum of transaction expense amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE categories=:categoryId "
            + "AND user_id=:ownerId "
            + "AND type =:expense")
    float getTotalIExpenseByCategoryId(String categoryId, String ownerId, String expense);

    @Query("SELECT AVG(monthly_sum) from (SELECT sum(amount) as monthly_sum, strftime('%Y-%m', date / 1000, 'unixepoch') as month from transactions " +
            " where type=:transactionType and user_id=:userId group by month )")
    double getMonthlyAvg( String transactionType, String userId);

    @Query("SELECT AVG(daily_sum) from (SELECT sum(amount) as daily_sum, date(date/1000, 'unixepoch', 'localtime') as day from transactions " +
            " where type=:transactionType and user_id=:userId group by day )")
    double getDailyAvg( String transactionType, String userId);

}
