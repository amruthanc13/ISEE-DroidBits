package com.droidbits.moneycontrol.db.transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transactions {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "amount")
    private Float amount;

    @ColumnInfo(name = "text_note", defaultValue = "")
    @Nullable
    private String textNote;

    // 1 - Expense, 2 - Income
    @ColumnInfo(name = "type", defaultValue = "1")
    private int type;

    // 1 - Cash, 2 - Card
    @ColumnInfo(name = "method", defaultValue = "1")
    private Integer method;

    @ColumnInfo(name = "date")
    private Long date;

    /**
     * Foreign keys
     */
    @ColumnInfo(name = "category")
    @Nullable
    private String category;

    /**
     * Constructor with amount, textNote, type, method, date and category
     * @param amount
     * @param textNote
     * @param type
     * @param method
     * @param date
     * @param category
     */
    @Ignore
    public Transactions(Float amount, @Nullable String textNote, int type, Integer method, Long date, @Nullable String category) {
        this.amount = amount;
        this.textNote = textNote;
        this.type = type;
        this.method = method;
        this.date = date;
        this.category = category;
    }

    /**
     * Empty constructor
     */
    public Transactions() {
    }

    //Todo: Add more constructors



    public int getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    /**
     * Getters
     **/
    @Nullable
    public String getTextNote() {
        return textNote;
    }

    public int getType() {
        return type;
    }

    public Integer getMethod() {
        return method;
    }

    public Long getDate() {
        return date;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * Setters
     **/

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setTextNote(@Nullable String textNote) {
        this.textNote = textNote;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }
}
