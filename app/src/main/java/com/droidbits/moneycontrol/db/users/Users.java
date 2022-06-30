package com.droidbits.moneycontrol.db.users;

import static androidx.room.ForeignKey.SET_NULL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.droidbits.moneycontrol.db.account.Account;

@Entity(tableName = "users",
        foreignKeys = {
                @ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "selected_account",
                        onDelete = SET_NULL
                ),
        },
        indices = {@Index("selected_account")}
)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    public class Users {

        @PrimaryKey(autoGenerate = true)
        private int id;

        @ColumnInfo(name = "first_name")
        @Nullable
        private String firstName;

        @ColumnInfo(name = "last_name")
        @Nullable
        private String lastName;

        @ColumnInfo(name = "email")
        @NonNull
        private String email;

        @ColumnInfo(name = "password")
        private String password;

        @ColumnInfo(name = "access_pin")
        private String accessPin;

        // Foreign keys
        @ColumnInfo(name = "selected_account")
        @Nullable
        private String selectedAccount;




    /**
         * Get user id.
         * @return user id.
         */
        public int getId() {
            return id;
        }


        /**
         * Get user email.
         * @return email.
         */
        public String getEmail() {
            return email;
        }

        /**
         * Get user first name.
         * @return first name.
         */
        @Nullable
        public String getFirstName() {
            return firstName;
        }

        /**
         * Get user last name.
         * @return last name.
         */
        @Nullable
        public String getLastName() {
            return lastName;
        }

        /**
         * Get user password.
         * @return password.
         */
        public String getPassword() {
            return password;
        }

        /**
         * Get user access pin.
         * @return access pin.
         */
        public String getAccessPin() {
            return accessPin;
        }

        /**
        * Get user's selected account.
        * @return account id.
        */
        @Nullable
        public String getSelectedAccount() {
        return selectedAccount;
    }



    /**
         * Set user id.
         * @param id id.
         */
        public void setId(final int id) {
            this.id = id;
        }

        /**
         * Set user email.
         * @param email email.
         */
        public void setEmail(final String email) {
            this.email = email;
        }

        /**
         * Set user first name.
         * @param firstName name.
         */
        public void setFirstName(final @Nullable String firstName) {
            this.firstName = firstName;
        }

        /**
         * Set user last name.
         * @param lastName last name.
         */
        public void setLastName(final @Nullable String lastName) {
            this.lastName = lastName;
        }

        /**
         * Set user password.
         * @param password password.
         */
        public void setPassword(final String password) {
            this.password = password;
        }

        /**
         * Set user access pin.
         * @param mAccessPin access pin.
         */
        public void setAccessPin(final String mAccessPin) {
            this.accessPin = mAccessPin;
        }


        /**
        * Set user's selected account.
        * @param selectedAcc account id.
        */
        public void setSelectedAccount(final @Nullable String selectedAcc) {
         this.selectedAccount = selectedAcc;
        }
}
