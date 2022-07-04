package com.droidbits.moneycontrol.ui.users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.droidbits.moneycontrol.R;

import com.droidbits.moneycontrol.db.account.Account;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.defaults.Defaults;
import com.droidbits.moneycontrol.db.users.Users;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.home.AccountViewModel;
import com.droidbits.moneycontrol.ui.home.HomeActivity;
import com.droidbits.moneycontrol.ui.settings.DefaultsViewModel;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private CategoriesViewModel categoriesViewModel;
    private UsersViewModel usersViewModel;
    private DefaultsViewModel defaultsViewModel;
    private AccountViewModel accountViewModel;
    private  Users user;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private View signInContainer;
    private View signUpContainer;
    private TextView currentUser;
    private LinearLayout selectedUser;
    private List<Users> userList;

    private Button changeUserButton;
    private Button signUpButton;
    private Button validateCredentialsButton;
    private Button createNewAccountButton;
    private Button alreadyHaveAnAccountButton;

    private TextInputEditText accessTokenValueText;
    private TextInputLayout enterAccessTokenInputGroup;
    private TextInputLayout emailInputGroup;
    private TextInputEditText emailInputText;
    private TextInputLayout accessTokenInputGroup;
    private TextInputEditText accessTokenInputText;
    private TextInputLayout repeatAccessTokenInputGroup;
    private TextInputEditText repeatAccessTokenInputText;
    private MaterialAlertDialogBuilder switchUserDialog;


    public static final String EMAIL_ADDRESS_PATTERN =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                    + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                    + "("
                    + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                    + ")+";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        setContentView(R.layout.sign_in);
        signInContainer = findViewById(R.id.signinContainer);
        signUpContainer = findViewById(R.id.signupContainer);

        currentUser = signInContainer.findViewById(R.id.currentUserEmail);
        selectedUser = signInContainer.findViewById(R.id.selectedUser);
        changeUserButton = signInContainer.findViewById(R.id.changeUserButton);

        accessTokenValueText = signInContainer.findViewById(R.id.accessTokenValueText);
        enterAccessTokenInputGroup = signInContainer.findViewById(R.id.enterAccessTokenInputGroup);

        createNewAccountButton = signInContainer.findViewById(R.id.createNewAccountButton);
        validateCredentialsButton = signInContainer.findViewById(R.id.validateCredentialsButton);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAnAccountButton = signUpContainer.findViewById(R.id.alreadyHaveAnAccountButton);
        signUpButton = signUpContainer.findViewById(R.id.signUpButton);

        emailInputGroup = signUpContainer.findViewById(R.id.emailInputGroup);
        emailInputText = signUpContainer.findViewById(R.id.emailInputText);

        accessTokenInputGroup = signUpContainer.findViewById(R.id.accessTokenInputGroup);
        accessTokenInputText = signUpContainer.findViewById(R.id.accessTokenInputText);

        repeatAccessTokenInputGroup = signUpContainer.findViewById(R.id.repeatAccessTokenInputGroup);
        repeatAccessTokenInputText = signUpContainer.findViewById(R.id.repeatAccessTokenInputText);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        usersViewModel.getAllUsers().observe(this, new Observer<List<Users>>() {
            @Override
            public void onChanged(final List<Users> users) {
                userList = users;

                if (users.size() > 0) {
                    showSignInContent();
                    initializeSwitchUserDialog();

                    String currentUserId = sharedPreferencesUtils.getCurrentUserId();
                    if (currentUserId.equals("")) {
                        currentUser.setText("No user selected.");
                    //    currentUser.setTextColor(getApplication().getColor(R.color.colorExpense));
                    } else {
                        setUser();
                    //    currentUser.setTextColor(getApplication().getColor(android.R.color.darker_gray));
                    }
                } else {
                    showSignUpContent();
                }
            }
        });

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showSignUpContent();
            }
        });

        alreadyHaveAnAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (userList.size() == 0) {
                    Toast.makeText(
                            getBaseContext(),
                            "No users registered! Please create an account.",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    showSignInContent();
                }
            }
        });

        validateCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                signUp();
            }
        });

        changeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switchUserDialog.show();
            }
        });

    }

    /**
     * Show sign up content.
     */
    private void showSignUpContent() {
        resetSignUpState();
        signInContainer.setVisibility(View.GONE);
        signUpContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Show sign in content.
     */
    private void showSignInContent() {
        resetSignInState();
        signInContainer.setVisibility(View.VISIBLE);
        signUpContainer.setVisibility(View.GONE);
    }

    /**
     * Set user locally and in the text.
     */
    private void setUser() {
        user = usersViewModel.getCurrentUser();
        currentUser.setText(user.getEmail());
    }

    /**
     * Clear sign up errors and values.
     */
    private void resetSignUpState() {
        emailInputText.setText("");
        accessTokenInputText.setText("");
        repeatAccessTokenInputText.setText("");

        emailInputGroup.setError(null);
        accessTokenInputGroup.setError(null);
        repeatAccessTokenInputGroup.setError(null);
    }

    /**
     * Clear sign in errors and values.
     */
    private void resetSignInState() {
        accessTokenValueText.setText("");
        enterAccessTokenInputGroup.setError(null);
    }

    /**
     * Initialize switch user dialog.
     */
    private void initializeSwitchUserDialog() {
        String[] userListEmails = new String[userList.size()];

        for (int i = 0; i < userList.size(); i++) {
            userListEmails[i] = userList.get(i).getEmail();
        }

        switchUserDialog = new MaterialAlertDialogBuilder(SignInActivity.this)
                .setTitle("Select user account")
                .setItems(userListEmails, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Users selected = userList.get(which);
                        sharedPreferencesUtils.setCurrentUserId(String.valueOf(selected.getId()));
                        setUser();
                    }
                    //test
                });

    }

    private void signIn() {
        if (accessTokenValueText.getText().toString().equals(user.getAccessPin())) {
            enterAccessTokenInputGroup.setError(null);

            sharedPreferencesUtils.setIsSignedIn(true);
            sharedPreferencesUtils.setCurrentUserId(String.valueOf(user.getId()));

            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
            finish();


        } else {
            enterAccessTokenInputGroup.setError("Access token is not valid.");
        }
    }

    /**
     * Execute sign in and move to home screen.
     */
    @SuppressWarnings({"checkstyle", "magicnumber"})
    private void signUp() {
        if (!emailInputText.getText().toString().matches(EMAIL_ADDRESS_PATTERN)) {
            emailInputGroup.setError("Please enter a valid email address.");
            return;
        } else {
            emailInputGroup.setError(null);
        }

        if (usersViewModel.getUserByEmail(emailInputText.getText().toString()) != null) {
            emailInputGroup.setError("Email already registered.");
            return;
        } else {
            emailInputGroup.setError(null);
        }

        if (accessTokenInputText.getText().toString().length() != 4) {
            accessTokenInputGroup.setError("Access token must be 4 digits long.");
            return;
        } else {
            accessTokenInputGroup.setError(null);
        }

        if (!repeatAccessTokenInputText.getText().toString().equals(accessTokenInputText.getText().toString())) {
            repeatAccessTokenInputGroup.setError("Access tokens do not match.");
            return;
        } else {
            repeatAccessTokenInputGroup.setError(null);
        }

        Users newUser = new Users();
        newUser.setAccessPin(accessTokenInputText.getText().toString());
        newUser.setEmail(emailInputText.getText().toString());

        long userId = usersViewModel.insert(newUser);
        sharedPreferencesUtils.setCurrentUserId(String.valueOf(userId));

        createDefaultUserAccount();
        createDefaultUserCategories();
        createDefaultUserCurrency();

    }

    private void createDefaultUserCategories() {

        Categories cinema = new Categories("Cinema", R.drawable.icon_cinema);
        Categories travel = new Categories("Travel", R.drawable.icon_travel);
        Categories shopping = new Categories("Shopping", R.drawable.icon_shopping);
        Categories dineOut = new Categories("Dine out", R.drawable.icon_dinner);
        Categories bill = new Categories("Bills", R.drawable.icon_bill);
        Categories drinks = new Categories("Drinks", R.drawable.icon_drinks);
        Categories incomeCategory = new Categories("Income", R.drawable.income);



        categoriesViewModel.insert(cinema);
        categoriesViewModel.insert(travel);
        categoriesViewModel.insert(shopping);
        categoriesViewModel.insert(dineOut);
        categoriesViewModel.insert(bill);
        categoriesViewModel.insert(drinks);
        categoriesViewModel.insert(incomeCategory);
    }

    private void createDefaultUserCurrency() {
        Defaults defaultCurrency = new Defaults("Currency", "EUR");
        Defaults defaultCategory = new Defaults("Category", "Cinema");
        Defaults defaultPayment = new Defaults("Payment", "Credit Card");
        Defaults defaultAddBtn1 = new Defaults("AddButton1", "50");
        Defaults defaultAddBtn2 = new Defaults("AddButton2", "100");

        defaultsViewModel.insert(defaultCurrency);
        defaultsViewModel.insert(defaultCategory);
        defaultsViewModel.insert(defaultPayment);
        defaultsViewModel.insert(defaultAddBtn1);
        defaultsViewModel.insert(defaultAddBtn2);
    }


    /**
     * Create default user account.
     */
    private void createDefaultUserAccount() {
        Account defaultAccount = new Account();

        defaultAccount.setName("Default account");

        Long newAccountId = accountViewModel.insert(defaultAccount);

        usersViewModel.updateUserSelectedAccount(String.valueOf(newAccountId));
        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(newAccountId));
    }
}
