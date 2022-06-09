package com.droidbits.moneycontrol.ui.users;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.users.Users;
import com.droidbits.moneycontrol.db.users.UsersRepository;

import java.util.List;


public class UsersViewModel extends AndroidViewModel {

    private UsersRepository repository;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public UsersViewModel(final Application application) {
        super(application);
        this.repository = new UsersRepository(application);
    }

    /**
     * Insert a new user in the database.
     * @param user transaction to be added.
     * @return user id.
     */
    public long insert(final Users user) {
        return repository.insert(user);
    }

    /**
     * Get all users from the database.
     * @return all id, first_name, last_name and email of users in the database.
     */
    public LiveData<List<Users>> getAllUsers() {
        return repository.getAllUsers();
    }

    /**
     * Get user by id.
     * @param userId id.
     * @return user.
     */
    public Users getUserById(final int userId) {
        Users user = repository.getUserById(userId);

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Get user by email.
     * @param userEmail email.
     * @return user.
     */
    public Users getUserByEmail(final String userEmail) {
        Users user = repository.getUserByEmail(userEmail);

        // TODO: add validation, user should be authenticated

        return user;
    };

}
