package com.droidbits.moneycontrol.db.currency;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDao {
    /**
     * Insert new currency.
     * @param currency currency to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Currency currency);

    /**
     * Delete all currencies.
     */
    @Query("DELETE FROM currencies")
    void deleteAll();

    /**
     * Retrieve all currencies.
     * @return list of currencies.
     */
    @Query("SELECT * FROM currencies")
    List<Currency> getAllCurrencies();

    /**
     * Retrieve all currency codes.
     * @return list of currency codes.
     */
    @Query("SELECT three_letter_code FROM currencies")
    String[] getAllCurrencyCodes();

    /**
     * Retrieve the currency symbol.
     * @return symbol of currency code.
     * @param code three letter code of currency.
     */
    @Query("SELECT symbol FROM currencies WHERE three_letter_code=:code")
    String getCurrencySymbol(String code);
}
