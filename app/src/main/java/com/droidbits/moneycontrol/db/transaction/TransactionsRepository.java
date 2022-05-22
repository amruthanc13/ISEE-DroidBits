package com.droidbits.moneycontrol.db.transaction;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class TransactionsRepository {
    private TransactionsDao transactionsDao;

    public TransactionsRepository(Application application){
        MoneyControlDB database = MoneyControlDB.getInstance(application);
        transactionsDao = database.transactionsDao();
    }

    /**
     * Get all transactions from the database.
     * @return all transactions in the database.
     */
    public LiveData<List<Transactions>> getAllTransactions() {

        return transactionsDao.getAllTransactions();
    }

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */
    public Transactions getTransactionById(final long transactionId) {

        return transactionsDao.getTransactionById(transactionId);
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
        return transactionsDao.insert(transaction);
    }

    /**
     * Update transaction recurring fields.
     */
    public void deleteAllTransactions() {

        transactionsDao.deleteAllTransactions();
    }


}
