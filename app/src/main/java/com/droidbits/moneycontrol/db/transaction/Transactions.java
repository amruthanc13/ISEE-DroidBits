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

    @ColumnInfo(name = "type", defaultValue = "Expense")
    private String type;

    @ColumnInfo(name = "method", defaultValue = "Cash")
    private String method;

    @ColumnInfo(name = "date")
    private Long date;

    /**
     * Foreign keys
     */
    @ColumnInfo(name = "categories")
    @Nullable
    private int category;

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
    public Transactions(Float amount, @Nullable String textNote, String type, String method, Long date, @Nullable int category) {
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

    /**
     * Transaction constructor with note.
     * @param amount transaction amount.
     * @param note transaction note.
     */
    @Ignore
    public Transactions(final float amount, final @Nullable String note) {
        this.amount = amount;
        this.textNote = note;
    }

    /**
     * Transaction constructor with note and category.
     * @param transAmount transaction amount.
     * @param note transaction note.
     * @param categoryId category id.
     */
    @Ignore
    public Transactions(final float transAmount, final @Nullable String note, final @Nullable int categoryId) {
        this.textNote = note;
        this.amount = transAmount;
        this.category = categoryId;
    }

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

    public String getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public Long getDate() {
        return date;
    }

    @Nullable
    public int getCategory() {
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

    public void setType(String type) {
        this.type = type;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setCategory(@Nullable int category) {
        this.category = category;
    }
}
