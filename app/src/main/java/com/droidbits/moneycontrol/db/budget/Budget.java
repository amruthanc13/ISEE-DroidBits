package com.droidbits.moneycontrol.db.budget;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.droidbits.moneycontrol.db.account.Account;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.users.Users;

@Entity(
        tableName = "budget",
        foreignKeys = {
                @ForeignKey(
                        entity = Categories.class,
                        parentColumns = "id",
                        childColumns = "categories",
                        onDelete = SET_NULL
                ),
                @ForeignKey(
                        entity = Users.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "account",
                        onDelete = CASCADE
                )
        },
        indices = {@Index("categories"), @Index("account")}
)
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private Float amount;

    /**
     * Foreign keys.
     */
    @ColumnInfo(name = "categories")
    @Nullable
    private String category;

    @ColumnInfo(name = "user_id", index = true)
    @Nullable
    private String userId;

    @ColumnInfo(name = "account")
    @Nullable
    private String account;

    public Budget(int id, Float amount, String category) {
        this.id = id;
        this.amount = amount;
        this.category = category;
    }

    @Ignore
    public Budget(Float amount, @Nullable String category) {
        this.amount = amount;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    @Nullable
    public String getAccount() {
        return account;
    }

    public void setAccount(final @Nullable String mAccount) {
        this.account = mAccount;
    }
}
