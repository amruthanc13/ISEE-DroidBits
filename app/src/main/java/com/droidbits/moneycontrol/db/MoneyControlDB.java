package com.droidbits.moneycontrol.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.budget.BudgetDao;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.categories.CategoriesDao;
import com.droidbits.moneycontrol.db.currency.Currency;
import com.droidbits.moneycontrol.db.currency.CurrencyDao;
import com.droidbits.moneycontrol.db.defaults.Defaults;
import com.droidbits.moneycontrol.db.defaults.DefaultsDao;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.users.Users;
import com.droidbits.moneycontrol.db.users.UsersDao;

@Database(entities = {Transactions.class, Categories.class, Budget.class, Users.class, Currency.class, Defaults.class}, version = 1)
public abstract class MoneyControlDB extends RoomDatabase {

    public abstract TransactionsDao transactionsDao();
    public abstract CategoriesDao categoriesDao();
    public abstract BudgetDao budgetDao();
    public abstract UsersDao usersDao();
    public abstract CurrencyDao currencyDao();
    public abstract DefaultsDao defaultsDao();

    private static volatile MoneyControlDB dbInstance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService DATABASE_WRITE_EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MoneyControlDB getInstance(Context context){
        if (dbInstance == null) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MoneyControlDB.class, "MoneyControl_db")
                            .addCallback(roomCallback)
                            .allowMainThreadQueries()
                            .build();
            }
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            DATABASE_WRITE_EXECUTOR.execute(MoneyControlDB::populateDatabase);

        }
    };

    private static void populateDatabase() {

        CurrencyDao currencyDao = dbInstance.currencyDao();

        Currency euro = new Currency("EUR", "€");
        Currency uDollar = new Currency("USD", "$");
        Currency yen = new Currency("JPY", "¥");
        Currency pound = new Currency("GBP", "£");
        Currency rupee = new Currency("INR", "₹");
        Currency won = new Currency("KRW", "₩");

        currencyDao.insert(euro);
        currencyDao.insert(uDollar);
        currencyDao.insert(yen);
        currencyDao.insert(pound);
        currencyDao.insert(rupee);
        currencyDao.insert(won);


    }

}

