package com.droidbits.moneycontrol.db.transaction;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

import androidx.annotation.NonNull;
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

@Entity(tableName = "transactions",
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
                        onDelete = SET_NULL
                ),
        },
        indices = {@Index("categories"), @Index("user_id"), @Index("account")}
)
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

    @ColumnInfo(name = "is_repeating", defaultValue = "0")
    @Nullable
    private Boolean isRepeating;

    @ColumnInfo(name = "repeating_interval_type", defaultValue = "1")
    @Nullable
    private Integer repeatingIntervalType;

    /**
     * Foreign keys.
     */
    @ColumnInfo(name = "categories")
    @Nullable
    private String category;

    @ColumnInfo(name = "user_id")
    @Nullable
    private String userId;

    @ColumnInfo(name = "account")
    @Nullable
    private String account;
    /**
     * Constructor with amount, textNote, type, method, date and category.
     * @param amount
     * @param textNote
     * @param type
     * @param method
     * @param date
     * @param category
     */
    @Ignore
    public Transactions(Float amount, @Nullable String textNote, String type, String method, Long date, @Nullable String category) {
        this.amount = amount;
        this.textNote = textNote;
        this.type = type;
        this.method = method;
        this.date = date;
        this.category = category;
    }

    /**
     * Constructor with amount, textNote, type, method, date and category.
     * @param amount
     * @param textNote
     * @param type
     * @param method
     * @param date
     * @param category
     * @param isRepeating
     * @param repeatingIntervalType
     */
    @Ignore
    public Transactions(Float amount, @Nullable String textNote, String type, String method, Long date, @Nullable String category, boolean isRepeating, int repeatingIntervalType) {
        this.amount = amount;
        this.textNote = textNote;
        this.type = type;
        this.method = method;
        this.date = date;
        this.category = category;
        this.isRepeating = isRepeating;
        this.repeatingIntervalType = repeatingIntervalType;
    }

    /**
     * Constructor with amount, textNote, type, method, date and category.
     * @param amount
     * @param textNote
     * @param type
     * @param method
     * @param date
     */
    @Ignore
    public Transactions(Float amount, @Nullable String textNote, String type, String method, Long date) {
        this.amount = amount;
        this.textNote = textNote;
        this.type = type;
        this.method = method;
        this.date = date;
    }

    /**
     * Empty constructor.
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
    public Transactions(final float transAmount, final @Nullable String note, final @Nullable String categoryId) {
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
    public String getCategory() {
        return category;
    }

    @Nullable
    public Boolean getIsRepeating() {
        return isRepeating;
    }

    @Nullable
    public Integer getRepeatingIntervalType() {
        return repeatingIntervalType;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    @Nullable
    public String getAccount() {
        return account;
    }

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

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    public void setRepeating(@Nullable Boolean repeating) {
        isRepeating = repeating;
    }

    public void setRepeatingIntervalType(@Nullable Integer repeatingIntervalType) {
        this.repeatingIntervalType = repeatingIntervalType;
    }

    /**
     * Recurring getter.
     * @return whether transaction is recurring.
     */
    public Boolean isTransactionRepeating() {
        return isRepeating;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", amount=" + amount +
                ", textNote='" + textNote + '\'' +
                ", type='" + type + '\'' +
                ", method='" + method + '\'' +
                ", date=" + date +
                ", isRepeating=" + isRepeating +
                ", repeatingIntervalType=" + repeatingIntervalType +
                ", category='" + category + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    /**
     * Set account id.
     * @param mAccount account id.
     */
    public void setAccount(final @Nullable String mAccount) {
        this.account = mAccount;
    }

}
