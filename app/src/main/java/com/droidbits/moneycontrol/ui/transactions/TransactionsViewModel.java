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

    public void insert(Transactions transactions){
        transactionsRepository.insert(transactions);
    }

    public void update(Transactions transactions){
        transactionsRepository.update(transactions);
    }

    public void delete(Transactions transactions){
        transactionsRepository.delete(transactions);
    }

    public void deleteAllTransactions(){
        transactionsRepository.deleteAllTransactions();
    }

    public LiveData<List<Transactions>> getAllTransactions(){
        return allTransactions;
    }
}
