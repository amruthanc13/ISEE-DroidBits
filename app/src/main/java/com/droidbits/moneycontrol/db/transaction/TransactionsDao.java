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
    long insert(Transactions transactions);

    @Query("DELETE from transactions WHERE id=:transactionId")
    void deleteTransaction(
            int transactionId
    );

    @Query("DELETE FROM transactions")
    void deleteAllTransactions();

    /**
     * Retrieve all transactions from the database.
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions "
            + "ORDER BY date DESC")
    LiveData<List<Transactions>> getAllTransactions();

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */
    @Query("SELECT * FROM transactions WHERE id=:transactionId ")
    Transactions getTransactionById(long transactionId);


}
