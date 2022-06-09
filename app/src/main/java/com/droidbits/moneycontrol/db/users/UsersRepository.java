package com.droidbits.moneycontrol.db.users;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.MoneyControlDB;

import java.util.List;

public class UsersRepository {
    private UsersDao usersDao;

    /**
     * Constructor.
     * @param application application.
     */
    public UsersRepository(final Application application) {
        MoneyControlDB database = MoneyControlDB.getInstance(application);
        usersDao = database.usersDao();
    }

    /**
     * Insert a new user in the database.
     * @param user user to be saved.
     * @return user id.
     */
    public long insert(final Users user) {
        return usersDao.insert(user);
    }

    /**
     * Get all users from the database.
     * @return all id, first_name, last_name and email of users in the database.
     */
    public LiveData<List<Users>> getAllUsers() {
        return usersDao.getAllUsers();
    }

    /**
     * Get user by id.
     * @param userId id.
     * @return user.
     */
    public Users getUserById(final int userId) {
        Users user = usersDao.getUserById(userId);

        // TODO: add validation, user should be authenticated

        return user;
    };


    /**
     * Get user by email.
     * @param userEmail email.
     * @return user.
     */
    public Users getUserByEmail(final String userEmail) {
        Users user = usersDao.getUserByEmail(userEmail);

        // TODO: add validation, user should be authenticated

        return user;
    };

}
