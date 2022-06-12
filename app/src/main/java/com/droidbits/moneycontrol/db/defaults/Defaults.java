package com.droidbits.moneycontrol.db.defaults;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidbits.moneycontrol.db.users.Users;

@Entity(
    tableName = "defaults",
    foreignKeys = {
        @ForeignKey(
            entity = Users.class,
            parentColumns = "id",
            childColumns = "user_id",
            onDelete = CASCADE
        ),
    },
    indices = {@Index("user_id")}
)
public class Defaults {

    @PrimaryKey(autoGenerate = true)
    private int defaultId;

    @ColumnInfo (name = "default_entity", defaultValue = "NULL")
    @Nullable
    private String defaultEntity;

    @ColumnInfo(name = "default_value", defaultValue = "NULL")
    @Nullable
    private String defaultValue;

    @ColumnInfo(name = "user_id")
    @Nullable
    private String userId;

    /**
     * Empty defaults constructor.
     */
    public Defaults() { }

    /**
     * Default constructor.
     * @param entity name String
     * @param value String
     */
    @Ignore
    public Defaults(@Nullable final String entity, @Nullable final String value) {
        this.defaultEntity = entity;
        this.defaultValue = value;
    }
    /**
     * ID getter.
     * @return id default.
     */
    public int getDefaultId() {
        return defaultId;
    }
    /**
     * ID setter.
     * @param id set.
     */
    public void setDefaultId(final int id) {
        this.defaultId = id;
    }
    /**
     * Entity getter.
     * @return entity returned.
     */
    @Nullable
    public String getDefaultEntity() {
        return defaultEntity;
    }
    /**
     * entity setter.
     * @param entity set.
     */
    public void setDefaultEntity(@Nullable final String entity) {
        this.defaultEntity = entity;
    }
    /**
     * value getter.
     * @return value of the corresponding default entity returned.
     */
    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }
    /**
     * value setter.
     * @param value of the default entity set.
     */
    public void setDefaultValue(@Nullable final String value) {
        this.defaultValue = value;
    }

    /**
     * Get user id.
     * @return user id.
     */
    @Nullable
    public String getUserId() {
        return userId;
    }

    /**
     * Set user id.
     * @param mUserId user id.
     */
    public void setUserId(final @Nullable String mUserId) {
        this.userId = mUserId;
    }

    /**
     * Parses class info to export csv format.
     * @return formatted string.
     */
    public String toExportString() {
        return this.defaultId
                + ", " + this.defaultEntity
                + ", " + this.defaultValue;
    }
}
