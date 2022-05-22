package com.droidbits.moneycontrol.db.categories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Categories {

    @PrimaryKey
    @NonNull
    private int id;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @Ignore
    public Categories(int id, @Nullable String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public Categories() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}


