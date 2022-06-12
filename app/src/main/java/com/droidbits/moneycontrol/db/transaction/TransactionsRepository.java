package com.droidbits.moneycontrol.db.transaction;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class TransactionsRepository {
    private TransactionsDao transactionsDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public TransactionsRepository(Application application){
        MoneyControlDB database = MoneyControlDB.getInstance(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        transactionsDao = database.transactionsDao();
    }

    /**
     * Get all transactions from the database.
     * @return all transactions in the database.
     */
    public LiveData<List<Transactions>> getAllTransactions() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        if (currentUserId.equals("")) {
            return null;
        }
        return transactionsDao.getAllTransactions(currentUserId);
    }

    /**
     * Retrieve Filtered transactions from the database.
     * @return Filtered transactions.
     */
    public List<Transactions> filterTransactions(Float amountFrom, Float amountTo, Long dateFrom, Long dateTo, String paymentMethod, String categoryId) {

        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        if (currentUserId.equals("")) {
            return null;
        }
        return transactionsDao.filterTransactions(amountFrom, amountTo, dateFrom, dateTo, paymentMethod, categoryId, currentUserId);
    }
    /**
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */
    public Transactions getTransactionById(final long transactionId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        if (currentUserId.equals("")) {
            return null;
        }
        return transactionsDao.getTransactionById(transactionId, currentUserId);
    };

    /**
     * Update transaction recurring fields.
     * @param transactionId id.
     */
    public void deleteTransaction(final int transactionId) {

        transactionsDao.deleteTransaction(
                transactionId
        );
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be saved.
     * @return transaction id.
     */
    public Long insert(final Transactions transaction) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        if (currentUserId.equals("")) {
            return null;
        }
        transaction.setUserId(currentUserId);
        return transactionsDao.insert(transaction);
    }

    /**
     * Update transaction recurring fields.
     */
    public void deleteAllTransactions() {

        transactionsDao.deleteAllTransactions();
    }

    public double getExpenseTransactionSum(){
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        return transactionsDao.getExpenseTransactionSum(currentUserId);
    }

    public double getIncomeTransactionSum(){
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        return transactionsDao.getIncomeTransactionSum(currentUserId);
    }

    /**
     * Update transaction recurring fields.
     * @param transactionId id.
     * @param isRepeating flag.
     * @param repeatingIntervalType type.
     */
    public void updateTransactionRepeatingFields(
            final int transactionId,
            final Boolean isRepeating,
            final Integer repeatingIntervalType
    ) {

        transactionsDao.updateTransactionRepeatingFields(
                transactionId,
                isRepeating,
                repeatingIntervalType);
    }

    /**
     * Update transaction amounts.
     * @param conversionRate conversion rate.
     */
    public void updateTransactionAmountsDefaultCurrency(final float conversionRate) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        transactionsDao.updateTransactionAmountsDefaultCurrency(conversionRate, currentUserId);
    }


    /**
     * Get sum of transaction income amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction income amount with matching categoryId
     */
    public Float getTotalIncomeByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String income = "Income";

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionsDao.getTotalIncomeByCategoryId(categoryId, currentUserId, income);
    }

    /**
     * Get sum of transaction expense amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction expense amount with matching categoryId
     */
    public Float getTotalIExpenseByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String expense = "Expense";

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionsDao.getTotalIExpenseByCategoryId(categoryId, currentUserId, expense);
    }

}
