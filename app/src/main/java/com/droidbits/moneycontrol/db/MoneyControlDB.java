package com.droidbits.moneycontrol.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.categories.CategoriesDao;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.droidbits.moneycontrol.R;

@Database(entities = {Transactions.class, Categories.class}, version = 1)
public abstract class MoneyControlDB extends RoomDatabase {

    public abstract TransactionsDao transactionsDao();
    public abstract CategoriesDao categoriesDao();

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

        CategoriesDao categoriesDao = dbInstance.categoriesDao();

        Categories cinema = new Categories(1, "Cinema", R.drawable.icon_cinema);
        Categories travel = new Categories(2, "Travel", R.drawable.icon_travel);
        Categories shopping = new Categories(3, "Shopping", R.drawable.icon_shopping);
        Categories dine_out = new Categories(4, "Dine out", R.drawable.icon_dinner);
        Categories bill = new Categories(5, "Bills", R.drawable.icon_bill);
        Categories drinks = new Categories(6, "Drinks", R.drawable.icon_drinks);

        categoriesDao.insert(cinema);
        categoriesDao.insert(travel);
        categoriesDao.insert(shopping);
        categoriesDao.insert(dine_out);
        categoriesDao.insert(bill);
        categoriesDao.insert(drinks);




    }

}

