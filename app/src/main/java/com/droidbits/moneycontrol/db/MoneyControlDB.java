package com.droidbits.moneycontrol.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsDao;

@Database(entities = {Transactions.class}, version = 1)
public abstract class MoneyControlDB extends RoomDatabase {

    public abstract TransactionsDao transactionsDao();

    private static volatile MoneyControlDB dbInstance;

    public static synchronized MoneyControlDB getInstance(Context context){
        if (dbInstance == null) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MoneyControlDB.class, "MoneyControl_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
            }
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TransactionsDao transactionsDao;

        private PopulateDbAsyncTask(MoneyControlDB db){
            transactionsDao = db.transactionsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}

