package com.droidbits.moneycontrol.ui.transactions;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.categories.AddCategory;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoryIconAdapter;
import com.droidbits.moneycontrol.ui.categories.CategoryTransactionAdapter;
import com.droidbits.moneycontrol.utils.DateUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Switch;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddTransactionFragment extends Fragment{
    private EditText tiedtTransactionAmount, tiedtTransactionNote;
    private EditText textCategory;
    private String transactionType;
    private String paymentMethod;
    private Long transactionDate;
    private TransactionsViewModel transactionViewModel;
    private CategoriesViewModel categoriesViewModel;
    private String categoryIconImage;
    private View currentView;
    private Button btnSave;
    private Spinner paymentSpinner, transactionTypeSpinner;
    private Transactions lastAddedTransaction;

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
        View v = inflater.inflate(R.layout.fragment_add_transactions, container,false);
        transactionTypeSpinner = v.findViewById((R.id.spinnerTransactionType));
        ArrayAdapter myadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.transaction_type));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSpinner.setAdapter(myadapter);

        //Transaction type on change
        transactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(transactionTypeSpinner.getSelectedItem().toString().equals("Income")){
                    textCategory.setVisibility(view.GONE);
                    textCategory.setText("Income");
                }
                else{
                    textCategory.setVisibility(view.VISIBLE);
                    textCategory.getText().clear();
                }
                transactionTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        isRepeatingSwitch = v.findViewById(R.id.repeatingSwitch);
        paymentSpinner = v.findViewById((R.id.spinnerPaymentMethod));
        ArrayAdapter myadapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.payment_method));
        myadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(myadapter2);

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        textCategory = v.findViewById(R.id.transactionCategory);

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

    /**
     * Initializes view elements.
     * @param view view
     * @param savedInstanceState instance state
     */
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {

        tiedtTransactionAmount = view.findViewById(R.id.transactionAmount);
        tiedtTransactionNote = view.findViewById(R.id.transactionNote);
        textCategory = view.findViewById(R.id.transactionCategory);

        transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        repeatingIntervalSpinner = view.findViewById(R.id.repeatingInterval);
        repeatingIntervalSpinner(view);

        isRepeatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                if (flag){
                    repeatingIntervalSpinner.setVisibility(view.VISIBLE);
                }
                else{
                    repeatingIntervalSpinner.setVisibility(view.GONE);
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
            tiedtTransactionAmount.setError("Please enter the amount of your transaction");
            requestFocus(tiedtTransactionAmount);
            return false;
        }

        if (Float.parseFloat(tiedtTransactionAmount.getText().toString().trim()) <= 0) {
            tiedtTransactionAmount.setError("Amount should be larger than 0");
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

    private String[] getDrawableNameFromIcon(int[] icons){
        List<String> listIconArray = new ArrayList<>();
        for (int icon:icons){
            listIconArray.add(getResources().getResourceEntryName(icon).split("icon_")[1]);
        }
        String[] stringIconArray = new String[listIconArray.size()];
        stringIconArray = listIconArray.toArray(stringIconArray);
        return stringIconArray;
    }

    private void setTransactionCategorySpinner(){
        List<Categories> categories = categoriesViewModel.getAllCategories();

        CategoryTransactionAdapter iconAdapter = new CategoryTransactionAdapter(getContext(), categories);
        iconAdapter.setListOfCategroies(categories);
        textCategory.setText(categories.get(0).getName());
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

    private void repeatingIntervalSpinner(final View view){
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

        if (transactionType.equals("Income") && textCategory.getText().toString().equals("Income")){
            return true;
        }

        else if (textCategory.getText().toString().trim().isEmpty()) {
            textCategory.setError("Please enter the category of your transaction");
            requestFocus(textCategory);
            return false;
        }

        else if (!Arrays.asList(dropdownItems).contains(textCategory.getText().toString().trim())) {
            textCategory.setError("Category is not valid");
            requestFocus(textCategory);
            return false;
        }
        return true;
    }


    /**
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        paymentMethod = paymentSpinner.getSelectedItem().toString();
        transactionType = transactionTypeSpinner.getSelectedItem().toString();
        isRepeating = isRepeatingSwitch.isChecked();

        Transactions newTransaction;
        if (!checkTransactionAmount()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }
        if (!checkTransactionCategory()){
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

        if (!checkRepeating()){
            return;
        }

        Log.d("String Text", "Value:" + paymentMethod);

        float transactionAmount = Float.parseFloat(tiedtTransactionAmount.getText().toString());
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

    /**
     * Process and create recurring transactions.
     * @param transaction initial transaction.
     * @return last transaction.
     */
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

        int frequency=0;
        int howMuchToAdd =1;

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

}