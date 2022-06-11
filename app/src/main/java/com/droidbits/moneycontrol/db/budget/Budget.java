package com.droidbits.moneycontrol.db.budget;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "budget"
)
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private Float amount;

    /**
     * Foreign keys
     */
    @ColumnInfo(name = "categories")
    @Nullable
    private String category;

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
    public String getCategory() {
        return category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }
}
