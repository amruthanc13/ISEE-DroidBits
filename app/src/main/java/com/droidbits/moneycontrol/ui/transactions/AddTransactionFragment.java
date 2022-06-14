package com.droidbits.moneycontrol.ui.transactions;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.currency.CurrencyDao;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.budget.BudgetViewModel;
import com.droidbits.moneycontrol.ui.categories.AddCategory;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoryTransactionAdapter;
import com.droidbits.moneycontrol.ui.settings.DefaultsViewModel;
import com.droidbits.moneycontrol.utils.DateUtils;
import com.droidbits.moneycontrol.utils.FormatterUtils;
import com.droidbits.moneycontrol.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddTransactionFragment extends Fragment {
    private static final String CURRENCY_DEFAULT_NAME = "Currency";
    private static final String CATEGORY_DEFAULT_NAME = "Category";
    private static final String PAYMENT_METHOD_DEFAULT_NAME = "Payment";
    private LinearLayout linearLayoutRecurruing;
    private TextInputEditText tiedtTransactionAmount;
    private TextInputLayout tilTransactionAmount;
    private EditText tiedtTransactionNote;
    private EditText textCategory;
    private EditText currencySpinner;
    private CurrencyDao currencyDao;
    private DefaultsViewModel defaultsViewModel;
    private String transactionType;
    private String paymentMethod;
    private Long transactionDate;
    private TransactionsViewModel transactionViewModel;
    private CategoriesViewModel categoriesViewModel;
    private BudgetViewModel budgetViewModel;
    private String categoryIconImage;
    private View currentView;
    private Button btnSave;
    private TextInputEditText transactionTypeSpinner;
    private TextInputEditText paymentSpinner;
    private Transactions lastAddedTransaction;
    private EditText budgetDialog;
    private RequestQueue requestQueue;
    private Float exchangeRate = 1f;

    private boolean isRepeating;
    private int repeatingInterval;
    private Switch isRepeatingSwitch;
    private EditText repeatingIntervalSpinner;

    private static DecimalFormat df = new DecimalFormat("#.00");

    private EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_transactions, container, false);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        //Transaction type on change
        setTransactionTypeSpinner(v);

        //payment type on change
        setTransactionMethodSpinner(v);

        isRepeatingSwitch = v.findViewById(R.id.repeatingSwitch);

        requestQueue = Volley.newRequestQueue(getContext());
        currencySpinner = v.findViewById(R.id.default_currency_spinner);
        currencyDao = MoneyControlDB.getInstance(getContext()).currencyDao();

        setDefaultCurrencySpinner();

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        textCategory = v.findViewById(R.id.transactionCategory);

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        //Set Transaction category spinner
        setTransactionCategorySpinner();

        AppCompatButton addTransactionLayout = v.findViewById(R.id.saveTransaction);
        addTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new TransactionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;

    }

    private void setTransactionTypeSpinner(final View view) {
        transactionTypeSpinner = view.findViewById((R.id.spinnerTransactionType));
        String[] dropdownItems = new String[]{"Expense", "Income"};

        transactionTypeSpinner.setText("Expense");
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction type")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        transactionTypeSpinner.setText(dropdownItems[which]);
                        if (transactionTypeSpinner.getText().toString().equals("Income")) {
                            textCategory.setVisibility(view.GONE);
                            textCategory.setText("Income");
                        } else {
                            textCategory.setVisibility(view.VISIBLE);
                            textCategory.getText().clear();
                        }
                    }
                });

        transactionTypeSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        transactionTypeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        transactionTypeSpinner.setInputType(0);
    }

    private void setTransactionMethodSpinner(final View view) {

        paymentSpinner = view.findViewById((R.id.spinnerPaymentMethod));
        String[] dropdownItems = new String[]{"Credit Card", "Debit Card", "Cash", "Paypal", "Other"};

        //set payment mode spinner value
        String defaultPaymentMode = defaultsViewModel.getDefaultValue(PAYMENT_METHOD_DEFAULT_NAME);
        paymentSpinner.setText(defaultPaymentMode);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the payment method")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        paymentSpinner.setText(dropdownItems[which]);

                    }
                });

        paymentSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        paymentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        paymentSpinner.setInputType(0);
    }

    /**
     * Initializes view elements.
     * @param view view
     * @param savedInstanceState instance state
     */
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {

        tiedtTransactionAmount = view.findViewById(R.id.transactionAmount);
        tilTransactionAmount = view.findViewById(R.id.til_transactionAmount);

        tiedtTransactionNote = view.findViewById(R.id.transactionNote);
        textCategory = view.findViewById(R.id.transactionCategory);

        transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        repeatingIntervalSpinner = view.findViewById(R.id.repeatingInterval);
        linearLayoutRecurruing = view.findViewById(R.id.recurringLayout);
        repeatingIntervalSpinner(view);

        isRepeatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                if (flag) {
                    linearLayoutRecurruing.setVisibility(view.VISIBLE);
                } else {
                    linearLayoutRecurruing.setVisibility(view.GONE);
                }
            }
        });


        btnSave = view.findViewById(R.id.saveTransaction);

        final Calendar myCalendar = Calendar.getInstance();

        etDate = view.findViewById(R.id.transactionDate);
        etDate.setText(DateUtils.formatDate(DateUtils.getStartOfCurrentDay().getTimeInMillis()));
        transactionDate = DateUtils.getStartOfCurrentDay().getTimeInMillis();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(etDate, myCalendar);
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        tiedtTransactionAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus) {
                    Editable editableContent = tiedtTransactionAmount.getText();

                    if (editableContent != null) {
                        try {
                            float value = Float.parseFloat(editableContent.toString());
                            df.setRoundingMode(RoundingMode.HALF_EVEN);
                            tiedtTransactionAmount.setText(df.format(value));
                        } catch (Exception e) { }
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });

    }
    /**
     * Method to focus on the view that have the wrong input.
     * @param view the view
     */
    public void requestFocus(final View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    /**
     * Method to check the input of transaction amount.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionAmount() {
        if (tiedtTransactionAmount.getText().toString().trim().isEmpty()) {
            tilTransactionAmount.setError("Please enter the amount");
            requestFocus(tiedtTransactionAmount);
            return false;
        }

        if (Float.parseFloat(tiedtTransactionAmount.getText().toString().trim()) <= 0) {
            tilTransactionAmount.setError("Amount should be larger than 0");
            requestFocus(tiedtTransactionAmount);
            return false;
        }
        return true;
    }

    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionDate() {
        if (etDate.getText().toString().trim().isEmpty()) {
            etDate.setError("Please enter the date of the transaction");
            requestFocus(etDate);
            return false;
        }
        return true;
    }

    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkRepeating() {
        if (isRepeatingSwitch.isChecked() && repeatingIntervalSpinner.getText().toString().trim().isEmpty()) {
            repeatingIntervalSpinner.setError("Choose Repeating Interval");
            return false;
        }
        return true;
    }
    /**
     * Method to update the transaction date with the selected date.
     * @param editTextLayout transactionDateText transaction date
     * @param myCalendar calendar the calendar to choose date
     */
    private void updateTransactionDateLabel(final EditText editTextLayout, final Calendar myCalendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        transactionDate = myCalendar.getTimeInMillis();

        editTextLayout.setText(sdf.format(transactionDate));
    }

    private String[] getDrawableNameFromIcon(int[] icons) {
        List<String> listIconArray = new ArrayList<>();
        for (int icon:icons) {
            listIconArray.add(getResources().getResourceEntryName(icon).split("icon_")[1]);
        }
        String[] stringIconArray = new String[listIconArray.size()];
        stringIconArray = listIconArray.toArray(stringIconArray);
        return stringIconArray;
    }

    private void setTransactionCategorySpinner() {
        List<Categories> categories = categoriesViewModel.getAllCategories();

        CategoryTransactionAdapter iconAdapter = new CategoryTransactionAdapter(getContext(), categories);
        iconAdapter.setListOfCategroies(categories);

        //set default category
        String defaultCategory = defaultsViewModel.getDefaultValue(CATEGORY_DEFAULT_NAME);
        textCategory.setText(defaultCategory);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setAdapter(iconAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        String iconName = categories.get(which).getName();
                        textCategory.setText(iconName);
                    }
                });
        textCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });
        textCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

    }

    private void repeatingIntervalSpinner(final View view) {
        String[] dropdownItems = {"Daily", "Weekly", "Monthly", "Yearly"};

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {

                        repeatingIntervalSpinner.setText(dropdownItems[which]);
                    }
                });

        repeatingIntervalSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        repeatingIntervalSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
    }

    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionCategory() {
        String[] dropdownItems = categoriesViewModel.getCategoriesName();

        if (transactionType.equals("Income") && textCategory.getText().toString().equals("Income")) {
            return true;
        } else if (textCategory.getText().toString().trim().isEmpty()) {
            textCategory.setError("Please enter the category of your transaction");
            requestFocus(textCategory);
            return false;
        } else if (!Arrays.asList(dropdownItems).contains(textCategory.getText().toString().trim())) {
            textCategory.setError("Category is not valid");
            requestFocus(textCategory);
            return false;
        }
        return true;
    }


    /**
     * Submit method to submit the input from user.
     */
    @SuppressWarnings({"checkstyle", "magicnumber"})
    private void submitForm() {
        paymentMethod = paymentSpinner.getText().toString();
        transactionType = transactionTypeSpinner.getText().toString();
        isRepeating = isRepeatingSwitch.isChecked();

        Transactions newTransaction;
        if (!checkTransactionAmount()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }
        if (!checkTransactionCategory()) {
            return;
        }

        if (isRepeatingSwitch.isChecked()) {
            switch (repeatingIntervalSpinner.getText().toString()) {
                case "Daily":
                    repeatingInterval = 1;
                    break;
                case "Weekly":
                    repeatingInterval = 2;
                    break;
                case "Monthly":
                    repeatingInterval = 3;
                    break;
                case "Yearly":
                    repeatingInterval = 4;
                    break;
                default:
                    repeatingInterval = 0;
            }
        } else {
            repeatingInterval = 0;
        }

        if (!checkRepeating()) {
            return;
        }

        Log.d("String Text", "Value:" + paymentMethod);

        float transactionAmount = Float.parseFloat(tiedtTransactionAmount.getText().toString());
        transactionAmount = transactionAmount / exchangeRate;
        String transactionNote  = tiedtTransactionNote.getText().toString().trim() + "";


        int category = categoriesViewModel.getSingleCategory(textCategory.getText().toString()).getId();
        newTransaction = new Transactions((float) transactionAmount,
                    transactionNote,
                    transactionType,
                    paymentMethod,
                    DateUtils.getStartOfDayInMS(transactionDate),
                    Integer.toString(category)
            );

        if (isRepeating) {
            newTransaction.setRepeating(true);
            newTransaction.setRepeatingIntervalType(repeatingInterval);

        } else {
            newTransaction.setRepeating(false);
            newTransaction.setRepeatingIntervalType(0);
        }

       checkBudget(Integer.toString(category), transactionAmount);


        //Insert new transaction  in to the database
        long newTransactionId = transactionViewModel.insert(newTransaction);
        Log.d("newTransactionId", "Value:" + newTransactionId);
        //Todo: checkBudget(category): (query to fetch sum of amt from transaction for this category =total expense,
        // budget = budgetViewModel.getSingleBudget(category), Budgetamt = budget.getAmout
        // if totalexpense>=budgetAmt : throw notificaton
        Transactions insertedTransaction = transactionViewModel.getTransactionById(newTransactionId);

        if (insertedTransaction.isTransactionRepeating()) {
            lastAddedTransaction = processAddRecurringTransaction(insertedTransaction);
        }

        Fragment fragment = new TransactionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        String total = "Added new transaction";
        Toast.makeText(getContext(), total, Toast.LENGTH_LONG).show();

}

    private void checkBudget( String category, float transactionAmt) {
        double totalExpense = transactionViewModel.getCategorySum(category);
        totalExpense += transactionAmt;
        System.out.print(totalExpense);
        double budgetAmount = budgetViewModel.getBudgetAmountByCategory(category);
        if ((budgetAmount!= 0) && (totalExpense >= budgetAmount)) {
            String message = "Budget limit exceeded";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getContext()
            )
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentTitle("New Notification")
                    .setContentText(message)
                    .setAutoCancel(true);

            NotificationManager mNotificationManager;
            mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Notification";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            mNotificationManager.notify(0,builder.build());
            System.out.print("You have exceeded the limit");
        }
    }

    /**
     * Process and create recurring transactions.
     * @param transaction initial transaction.
     * @return last transaction.
     */
    @SuppressWarnings({"checkstyle", "magicnumber"})
    private Transactions processAddRecurringTransaction(final Transactions transaction) {
        Transactions copyTransaction = new Transactions();

        Integer repeatingIntervalType = transaction.getRepeatingIntervalType();

        Calendar repeatingDate = DateUtils.getStartOfDay(transaction.getDate());
        Calendar today = DateUtils.getStartOfCurrentDay();
        int counter = 0;

        copyTransaction.setAmount(transaction.getAmount());
        copyTransaction.setCategory(transaction.getCategory());
        copyTransaction.setMethod(transaction.getMethod());
        copyTransaction.setType(transaction.getType());
        copyTransaction.setTextNote(transaction.getTextNote());
        copyTransaction.setRepeating(transaction.getIsRepeating());
        copyTransaction.setRepeatingIntervalType(repeatingIntervalType);
        copyTransaction.setId(transaction.getId());
        copyTransaction.setDate(transaction.getDate());

        int frequency = 0;
        int howMuchToAdd = 1;

        if (repeatingIntervalType != null) {

            switch (repeatingIntervalType) {
                case 1:
                    frequency = Calendar.DAY_OF_YEAR;
                    break;
                case 2:
                    frequency = Calendar.WEEK_OF_YEAR;
                    break;
                case 3:
                    frequency = Calendar.MONTH;
                    break;
                case 4:
                    frequency = Calendar.YEAR;
                    break;
            }

            do {
                repeatingDate.add(frequency, howMuchToAdd);

                if (repeatingDate.getTimeInMillis() <= today.getTimeInMillis()) {
                    // add a new transaction
                    // save currentTransaction as non-repeating.

                    copyTransaction.setRepeating(false);
                    copyTransaction.setRepeatingIntervalType(0);

                    transactionViewModel.updateTransactionRepeatingFields(
                            copyTransaction.getId(),
                            copyTransaction.getIsRepeating(),
                            copyTransaction.getRepeatingIntervalType()
                    );

                    Transactions newTransaction = new Transactions();

                    newTransaction.setAmount(copyTransaction.getAmount());
                    newTransaction.setCategory(copyTransaction.getCategory());
                    newTransaction.setMethod(copyTransaction.getMethod());
                    newTransaction.setType(copyTransaction.getType());
                    newTransaction.setTextNote(copyTransaction.getTextNote());
                    newTransaction.setRepeating(true);
                    newTransaction.setRepeatingIntervalType(repeatingIntervalType);

                    newTransaction.setDate(DateUtils.getStartOfDay(repeatingDate.getTimeInMillis()).getTimeInMillis());

                    long newTransactionId = transactionViewModel.insert(newTransaction);

                    copyTransaction = transactionViewModel.getTransactionById(newTransactionId);

                    counter++;
                }

            } while (repeatingDate.getTimeInMillis() <= today.getTimeInMillis());
        }

        Log.v("REPEATING", "TRANSACTIONS ADDED: " + (counter + 1));

        return copyTransaction;
    }

    private void setDefaultCurrencySpinner() {
        String[] dropdownItems = currencyDao.getAllCurrencyCodes();

        //set currency spinner value

        String stringCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);
        currencySpinner.setText(stringCurrency);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction currency")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        updateDefaultCurrency(dropdownItems[which]);
                    }
                });

        currencySpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        currencySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        currencySpinner.setInputType(0);
    }
    private void updateDefaultCurrency(final String newCurrency) {
        if (!NetworkUtils.isNetworkConnectionAvailable(getContext())) {
            Toast.makeText(
                    getContext(),
                    "No network connectivity, can't change the default currency!",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        requestExchangeRate(newCurrency);
    }

    private void requestExchangeRate(final String targetCurrency) {
        String originalCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);

        MaterialAlertDialogBuilder exchangeRateDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Update default currency?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Toast.makeText(getContext(), "Update of default currency cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (!originalCurrency.equals(targetCurrency)) {
                            // exchangeRate = getExchangeRate(originalCurrency,targetCurrency);
                            if (exchangeRate != null) {
                                currencySpinner.setText(targetCurrency);
                            }
                        }
                    }
                });

        String requestUrl = "https://api.exchangerate.host/latest?base="
                + originalCurrency
                + "&symbols="
                + targetCurrency;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            exchangeRate = Float.parseFloat(
                                    response.getJSONObject("rates").get(targetCurrency).toString()
                            );
                            exchangeRateDialogBuilder
                                    .setMessage("Exchange rate: 1 "
                                            + originalCurrency
                                            + " = "
                                            + FormatterUtils.roundToFourDecimals(exchangeRate)
                                            + " "
                                            + targetCurrency
                                    ).show();
                        } catch (final JSONException e) {
                            System.out.println(e);
                            Toast.makeText(
                                    getContext(),
                                    "Something went wrong, please try again!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                // TODO: Handle error
                Toast.makeText(
                        getContext(),
                        "Something went wrong, please try again!",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}