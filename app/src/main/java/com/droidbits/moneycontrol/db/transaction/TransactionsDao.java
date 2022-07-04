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
     * @param userId
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE user_id=:userId "
            + "AND account=:accountId "
            + "ORDER BY date DESC")
    LiveData<List<Transactions>> getAllTransactions(String userId, String accountId);

    @Query("SELECT * FROM transactions")
    List<Transactions> getAllTransactionsExportData();

    /**
     * get sum of expenses.
     * @param userId
     * @return sum
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Expense' AND user_id=:userId and account=:accountId")
    double getExpenseTransactionSum(String userId, String accountId);

    /**
     * get sum of income.
     * @param userId
     * @return sum
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Income' AND user_id=:userId and account=:accountId")
    double getIncomeTransactionSum(String userId, String accountId);

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @param userId
     * @return transaction.
     */

    @Query("SELECT * FROM transactions WHERE id=:transactionId AND user_id=:userId and account=:accountId")
    Transactions getTransactionById(long transactionId, String userId, String accountId);

    @Query("SELECT * FROM transactions "
            + "WHERE user_id=:userId "
            + "AND categories =:categoryId "
            + "AND account=:accountId "
            + "ORDER BY date DESC")
    LiveData<List<Transactions>> getTransactionsForCategory(String categoryId, String userId, String accountId);

    /**
     * get sum.
     * @param categoryId
     * @param userId
     * @return sum
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE categories =:categoryId AND user_id=:userId and account=:accountId")
    double getCategorySum(String categoryId, String userId, String accountId);

    /**
     * Retrieve Filtered transactions from the database.
     * @param userId
     * @param amountFrom
     * @param amountTo
     * @param categoryId
     * @param dateFrom
     * @param dateTo
     * @param paymentMethod
     * @return Filtered transactions.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE amount>=:amountFrom "
            + "AND  amount<=:amountTo "
            + "AND (date BETWEEN :dateFrom AND :dateTo) "
            + "AND (:paymentMethod IS NULL OR method =:paymentMethod) "
            + "AND (:categoryId IS NULL OR categories =:categoryId) "
            + "AND user_id =:userId "
            + "AND account =:accountId "
            + "ORDER BY date DESC")
    List<Transactions> filterTransactions(
            Float amountFrom,
            Float amountTo,
            Long dateFrom,
            Long dateTo,
            String paymentMethod,
            String categoryId,
            String userId,
            String accountId);

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
     * @param income
     * @return sum of transaction income amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE categories=:categoryId "
            + "AND user_id=:ownerId "
            + "AND account =:accountId "
            + "AND type =:income")
    float getTotalIncomeByCategoryId(String categoryId, String ownerId, String income, String accountId);

    /**
     * Retrieve sum of transaction expense amount for a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @param expense
     * @return sum of transaction expense amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE categories=:categoryId "
            + "AND user_id=:ownerId "
            + "AND type =:expense"
            +" AND account=:accountId")
    float getTotalIExpenseByCategoryId(String categoryId, String ownerId, String expense, String accountId);

    /**
     * get monthly avg.
     * @param transactionType
     * @param userId
     * @return double
     */
    @Query("SELECT AVG(monthly_sum) from (SELECT sum(amount) as monthly_sum, strftime('%Y-%m', date / 1000, 'unixepoch') as month from transactions "
            + " where type=:transactionType and user_id=:userId and account=:accountId group by month )")
    double getMonthlyAvg(String transactionType, String userId, String accountId);

    /**
     * get daily avg.
     * @param transactionType
     * @param userId
     * @return double
     */
    @Query("SELECT AVG(daily_sum) from (SELECT sum(amount) as daily_sum, date(date/1000, 'unixepoch', 'localtime') as day from transactions "
            + " where type=:transactionType and user_id=:userId and account=:accountId group by day )")
    double getDailyAvg(String transactionType, String userId, String accountId);

}
