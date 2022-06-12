package com.droidbits.moneycontrol.db.currency;

import android.app.Application;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class CurrencyRepository {

    private CurrencyDao currencyDao;
    private List<Currency> allCurrencies;

    /**
     * Currency repository constructor.
     * @param application application to be used.
     */
    CurrencyRepository(final Application application) {
        MoneyControlDB db = MoneyControlDB.getInstance(application);
        currencyDao = db.currencyDao();
        allCurrencies = currencyDao.getAllCurrencies();
    }

    /**
     * Get all currencies.
     * @return all saved currencies.
     */
    public List<Currency> getAllCurrencies() {
        return allCurrencies;
    }

    /**
     * Insert single currency.
     * @param currency currency to be saved.
     */
    public void insert(final Currency currency) {
        MoneyControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            currencyDao.insert(currency);
        });
    }
}
