package com.droidbits.moneycontrol.ui.transactions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsRepository;

import java.util.List;

public class TransactionsViewModel extends AndroidViewModel {
    private TransactionsRepository transactionsRepository;
    private LiveData<List<Transactions>> allTransactions;

    public TransactionsViewModel(@NonNull Application application){
        super(application);
        transactionsRepository = new TransactionsRepository(application);
        allTransactions = transactionsRepository.getAllTransactions();
    }

    /**
     * Insert a new transaction in the database.
     * @param transactions transaction to be added.
     * @return id of inserted transaction.
     */
    public long insert(Transactions transactions){
        return transactionsRepository.insert(transactions);
    }

    /**
     * Get single transaction by is.
     * @param id id.
     * @return transaction
     */
    public Transactions getTransactionById(final long id) {
        return transactionsRepository.getTransactionById(id);
    }

    /**
     * Get all transactions.
     * @return LiveData of transaction list.
     */
    public LiveData<List<Transactions>> getTransactions() {
        return allTransactions;
    }

    /**
     * Retrieve Filtered transactions from the database.
     * @return Filtered transactions.
     */
    public List<Transactions> filterTransactions(Float amountFrom, Float amountTo, Long dateFrom, Long dateTo, String paymentMethod, String categoryId) {

        return transactionsRepository.filterTransactions(amountFrom, amountTo, dateFrom, dateTo, paymentMethod, categoryId);
    }

    /**
     * Delete Transaction by transactionId
     * @param transactionId id.
     */
    public void deleteTransaction(
            final int transactionId
    ) {
        transactionsRepository.deleteTransaction(
                transactionId
        );
    }


    /**
     * Delete All Transactions
     */
    public void deleteAllTransactions(){
        transactionsRepository.deleteAllTransactions();
    }

    public double getTransactionSum(){
        return transactionsRepository.getTransactionSum();
    }
    /**
     * Get all transactions.
     * @return LiveData of transaction list.
     */
    public LiveData<List<Transactions>> getAllTransactions(){
        return allTransactions;
    }

    public void updateTransactionRepeatingFields(
            final int transactionId,
            final Boolean isRepeating,
            final Integer repeatingIntervalType
    ) {
        transactionsRepository.updateTransactionRepeatingFields(
                transactionId,
                isRepeating,
                repeatingIntervalType
        );
    }

    /**
     * Update transaction amounts.
     * @param conversionRate conversion rate.
     */
    public void updateTransactionAmountsDefaultCurrency(final float conversionRate) {
        transactionsRepository.updateTransactionAmountsDefaultCurrency(conversionRate);
    }
}
