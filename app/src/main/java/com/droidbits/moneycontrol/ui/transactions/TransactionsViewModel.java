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
     * Delete Transaction by transactionId
     * @param transactionId id.
     */
/*    public void delete(final int transactionId){
        transactionsRepository.delete(transactionId);
    }*/

    /**
     * Delete All Transactions
     */
    public void deleteAllTransactions(){
        transactionsRepository.deleteAllTransactions();
    }

    /**
     * Get all transactions.
     * @return LiveData of transaction list.
     */
    public LiveData<List<Transactions>> getAllTransactions(){
        return allTransactions;
    }
}
