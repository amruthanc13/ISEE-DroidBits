package com.droidbits.moneycontrol.db.categories;


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
    tableName = "categories",
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
public class Categories {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "icon")
    @Nullable
    private int icon;


    // Foreign keys
    @ColumnInfo(name = "user_id")
    @Nullable
    private String userId;


    /**
     * Category constructor.
     * @param categoryId id int
     * @param categoryName name String
     * @param categoryIcon icon int
     */
    public Categories(final int categoryId, final String categoryName, final int categoryIcon) {
        this.id = categoryId;
        this.name = categoryName;
        this.icon = categoryIcon;
    }

    /**
     * Empty category constructor.
     */
    public Categories() { }

    /**
     * Category constructor.
     * @param categoryName category name.
     */
    @Ignore
    public Categories(final String categoryName) {
        this.name = categoryName;
    }

    /**
     * Category constructor.
     * @param categoryName category name.
     * @param iconName icon name.
     */
    @Ignore
    public Categories(final String categoryName, final int iconName) {
        this.name = categoryName;
        this.icon = iconName;
    }

    /**
     * ID getter.
     * @return category id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Name getter.
     * @return category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Icon getter.
     * @return icon id.
     */
    public int getIcon() {
        return icon;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    /**
     * Category id setter.
     * @param categoryId category id.
     */
    public void setId(final int categoryId) {
        this.id = categoryId;
    }

    /**
     * Category name setter.
     * @param catName category name.
     */
    public void setName(final String catName) {
        this.name = catName;
    }

    /**
     * Category icon setter.
     * @param categoryIcon category icon id.
     */
    public void setIcon(final int categoryIcon) {
        this.icon = categoryIcon;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

}
