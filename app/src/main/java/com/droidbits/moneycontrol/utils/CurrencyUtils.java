package com.droidbits.moneycontrol.utils;

import java.util.Locale;

public final class CurrencyUtils {

    private static final Locale DEFAULT_LOCALE = Locale.GERMANY;
    public static final String DEFAULT_CURRENCY = "€";

    /**
     * Private constructor to prevent extending utility classes.
     */
    private CurrencyUtils() { }

    /**
     * Formats amount with default currency.
     * @param amount amount to be formatted.
     * @return formatted amount.
     */
    public static String formatAmount(final float amount) {
        return String.format(DEFAULT_LOCALE, DEFAULT_CURRENCY + " %.2f", amount);
    }

}
