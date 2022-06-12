package com.droidbits.moneycontrol.db.defaults;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DefaultsDao {
    /**
     * Insert new default.
     * @param defaults value to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Defaults defaults);

    /**
     * Delete all defaults.
     * @param userId user ID
     */
    @Query("DELETE FROM defaults WHERE user_id=:userId")
    void deleteAll(String userId);

    /**
     * Retrieve all defaults values.
     * @param userId user id.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults WHERE user_id=:userId")
    List<Defaults> getAllDefaults(String userId);

    /**
     * Get defaults for export csv.
     * @param userId user id.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults WHERE user_id=:userId ORDER BY defaultId ASC")
    List<Defaults> getDefaultsForExport(String userId);

    /**
     * Retrieve all defaults category.
     * @param name entity name.
     * @param userId user id.
     * @return string of defaults.
     */
    @Query("SELECT default_value FROM defaults WHERE default_entity=:name AND user_id=:userId")
    String getDefaultValue(String name, String userId);

    /**
     * Retrieve all defaults category.
     * string of defaults.
     */
    @Query("DELETE FROM defaults")
    void deleteAll();

    /**
     * Retrieve default currency symbol.
     * @param currency three letter currency code
     * @return string of currency symbol
     */
    @Query("SELECT symbol from currencies WHERE three_letter_code=:currency")
    String getCurrencySymbol(String currency);

    /**
     * Update default value.
     * @param defaultName default name.
     * @param newDefaultValue new value.
     * @param userId user id.
     */
    @Query("UPDATE defaults SET default_value=:newDefaultValue WHERE default_entity=:defaultName AND user_id=:userId")
    void updateDefaultValue(String defaultName, String newDefaultValue, String userId);
}
