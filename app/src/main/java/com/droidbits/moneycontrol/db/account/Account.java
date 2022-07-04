package com.droidbits.moneycontrol.db.account;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidbits.moneycontrol.db.users.Users;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "accounts",
        foreignKeys = @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "owner_id",
                onDelete = CASCADE
        ),
        indices = {@Index("owner_id")}
)
public class Account {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    @Nullable
    private int color;

    // Foreign keys
    @ColumnInfo(name = "owner_id")
    @Nullable
    private String ownerId;

    /**
     * Empty constructor.
     */
    public Account() { }

    /**
     * Get account id.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get account name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get account color.
     * @return color.
     */
    @Nullable
    public int getColor() {
        return color;
    }

    /**
     * Get account owner id.
     * @return owner id.
     */
    @Nullable
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Set account id.
     * @param mId account id.
     */
    public void setId(final int mId) {
        this.id = mId;
    }

    /**
     * Set account name.
     * @param mName name.
     */
    public void setName(final String mName) {
        this.name = mName;
    }

    /**
     * Set account color.
     * @param mColor color.
     */
    public void setColor(final @Nullable int mColor) {
        this.color = mColor;
    }

    /**
     * Set account owner id.
     * @param mOwnerId owner id.
     */
    public void setOwnerId(final @Nullable String mOwnerId) {
        this.ownerId = mOwnerId;
    }

    /**
     * Parses class info to export csv format.
     * @return formatted string.
     */
    public String toExportString() {
        return this.id + ", " + this.name + ", " + this.color;
    }
}
