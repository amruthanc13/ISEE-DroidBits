package com.droidbits.moneycontrol.db.transaction;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class TransactionsRepository {
    private TransactionsDao transactionsDao;
    private LiveData<List<Transactions>> allTransactions;

    public TransactionsRepository(Application application){
        MoneyControlDB database = MoneyControlDB.getInstance(application);
        transactionsDao = database.transactionsDao();
        allTransactions = transactionsDao.getAllTransactions();
    }

    public void insert(Transactions transactions){
        new InsertTransactionsAsyncTask(transactionsDao).execute(transactions);

    }
    public void update(Transactions transactions){
        new UpdateTransactionsAsyncTask(transactionsDao).execute(transactions);

    }
    public void delete(Transactions transactions){
        new DeleteTransactionsAsyncTask(transactionsDao).execute(transactions);

    }
    public void deleteAllTransactions (){
        new DeleteAllTransactionsAsyncTask(transactionsDao).execute();

    }

    public LiveData<List<Transactions>> getAllTransactions() {
        return allTransactions;
    }

    private static class InsertTransactionsAsyncTask extends AsyncTask<Transactions, Void, Void>{
        private TransactionsDao transactionsDao;
        private InsertTransactionsAsyncTask(TransactionsDao transactionsDao){
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions){
            transactionsDao.insert(transactions[0]);
            return null;
        }
    }

    private static class UpdateTransactionsAsyncTask extends AsyncTask<Transactions, Void, Void>{
        private TransactionsDao transactionsDao;
        private UpdateTransactionsAsyncTask(TransactionsDao transactionsDao){
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions){
            transactionsDao.update(transactions[0]);
            return null;
        }
    }

    private static class DeleteTransactionsAsyncTask extends AsyncTask<Transactions, Void, Void>{
        private TransactionsDao transactionsDao;
        private DeleteTransactionsAsyncTask(TransactionsDao transactionsDao){
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions){
            transactionsDao.delete(transactions[0]);
            return null;
        }
    }

    private static class DeleteAllTransactionsAsyncTask extends AsyncTask<Void, Void, Void>{
        private TransactionsDao transactionsDao;
        private DeleteAllTransactionsAsyncTask(TransactionsDao transactionsDao){
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Void... voids){
            transactionsDao.deleteAllTransactions();
            return null;
        }
    }
}
