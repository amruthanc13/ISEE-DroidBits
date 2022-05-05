package com.droidbits.moneycontrol.db.transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert
    void insert(Transactions transactions);

    @Update
    void update(Transactions transactions);

    @Delete
    void delete(Transactions transactions);

    @Query("DELETE FROM transactions")
    void deleteAllTransactions();

    /**
     * Retrieve all transactions from the database.
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions "
            + "ORDER BY date DESC")
    LiveData<List<Transactions>> getAllTransactions();

}
