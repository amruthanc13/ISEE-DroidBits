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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransactionFragment extends Fragment {

    private TextInputEditText tiedtTransactionAmount, tiedtTransactionNote;
    private TextView textCategory;
    private TextInputLayout tilTransactionAmount, tilTransactionNote, tilCategory;
    private int transactionType;
    private int transactionMethod;
    private TextInputEditText dropdownTransactionType;
    private TextInputEditText dropdownTransactionMethod;
    private TextInputEditText dropdownTransactionCategory;
    private TextInputEditText editText;
    private Long transactionDate;

    private TransactionsViewModel transactionsViewModel;

    private static DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_transactions, container,false);
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState){

        tiedtTransactionAmount = view.findViewById(R.id.tiedt_transactionAmount);
        tiedtTransactionNote = view.findViewById(R.id.tiedt_transactionNote);
        tilTransactionAmount = view.findViewById(R.id.til_transactionAmount);
        tilTransactionNote = view.findViewById(R.id.til_transactionNote);
        tilCategory = view.findViewById(R.id.tilCategory);
        textCategory = view.findViewById(R.id.categoryText);



        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        //set the spinner for transactionType from the xml.
        setTransactionTypeSpinner(view);
        //set the spinner for transactionMethod from the xml.
        setTransactionMethodSpinner(view);
        //set the spinner for transactionIcon from the xml.
        setTransactionCategorySpinner(view);


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

        final Calendar myCalendar = Calendar.getInstance();

        //Set Default Date
        editText = view.findViewById(R.id.transactionDate);
     //   editText.setText(DateUtils.formatDate(DateUtils.getStartOfCurrentDay().getTimeInMillis()));
       // transactionDate = DateUtils.getStartOfCurrentDay().getTimeInMillis();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(editText, myCalendar);
            }

        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
    /**
     * This method to set the spinner of Transaction Type.
     * @param view the transaction add layout
     */
    private void setTransactionTypeSpinner(final View view) {

        dropdownTransactionType = view.findViewById(R.id.spinnerTransactionType);
        String[] dropdownItems = new String[]{"Expense", "Income"};

        dropdownTransactionType.setText("Expense");
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction type")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownTransactionType.setText(dropdownItems[which]);
                        if (dropdownTransactionType.getText().toString().equals("Income")) {
                            tilCategory.setVisibility(view.GONE);
                            textCategory.setVisibility(view.GONE);
                            dropdownTransactionCategory.setText("Income");
                        } else {
                            tilCategory.setVisibility(view.VISIBLE);
                            textCategory.setVisibility(view.VISIBLE);
                            //Todo : Edit this
                          //  String defaultCategory = defaultsViewModel.getDefaultValue("Category");
                          //  dropdownTransactionCategory.setText(defaultCategory);
                        }
                    }
                });

        dropdownTransactionType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownTransactionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownTransactionType.setInputType(0);
    }

    /**
     * This method to set the spinner of Transaction Method.
     * @param view the transaction add layout
     */
    private void setTransactionMethodSpinner(final View view) {

        dropdownTransactionMethod = view.findViewById(R.id.spinnerTransactionMethod);
        String[] dropdownItems = new String[]{"Cash", "Credit Card", "Digital Wallet"};

        //set payment mode spinner value
        //ToDo : edit this
       // String defaultPaymentMode = defaultsViewModel.getDefaultValue("Payment Mode");
      //  dropdownTransactionMethod.setText(defaultPaymentMode);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the payment method")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownTransactionMethod.setText(dropdownItems[which]);

                    }
                });

        dropdownTransactionMethod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownTransactionMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownTransactionMethod.setInputType(0);
    }

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setTransactionCategorySpinner(final View view) {

        //Todo
        dropdownTransactionCategory = view.findViewById(R.id.spinnerTransactionCategory);
      //  String[] dropdownItems = categoryViewModel.getCategoriesName();

        //set payment mode spinner value
      //  String defaultCategory = defaultsViewModel.getDefaultValue("Category");
     //   dropdownTransactionCategory.setText(defaultCategory);

      /*  MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownTransactionCategory.setText(dropdownItems[which]);
                    }
                });*/

/*        dropdownTransactionCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });*/

     /*   dropdownTransactionCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });*/
        dropdownTransactionCategory.setInputType(0);
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
            tilTransactionAmount.setError("Please enter the amount of your transaction");
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
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please enter the date of the transaction");
            requestFocus(editText);
            return false;
        }
        return true;
    }

    /**
     * Method to convert transactionType.
     */
    private void convertTransactionType() {
        String text = dropdownTransactionType.getText().toString();
        if (text.equals("Income")) {
            transactionType = 2;
        } else {
            transactionType = 1;
        }
    }

    /**
     * Method to convert transactionType.
     */
    private void convertTransactionMethod() {
        String text = dropdownTransactionMethod.getText().toString().trim();
        Log.d("String Text", "Value:" + text);
        if (text.equals("Cash")) {
            transactionMethod = 1;
        } else if (text.equals("Credit Card")) {
            transactionMethod = 2;
        } else {
            transactionMethod = 3;
        }
    }

    /**
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        if (!checkTransactionAmount()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }

        convertTransactionType();
        convertTransactionMethod();

        String transactionCategory = dropdownTransactionCategory.getText().toString();

        //String transactionMethod = dropdownTransactionMethod.getText().toString();
        Log.d("String Text", "Value:" + transactionMethod);

        float transactionAmount = Float.parseFloat(tiedtTransactionAmount.getText().toString());

        String transactionNote  = tiedtTransactionNote.getText().toString().trim() + "";
        Transactions newTransaction = new Transactions((float) transactionAmount,
                transactionType,
                DateUtils.getStartOfDayInMS(transactionDate),
                transactionNote,
                "",
                "",
                transactionMethod
        );


        //Insert new Category in to the database
        long newTransactionId = transactionsViewModel.insert(newTransaction);

        Transactions insertedTransaction = transactionsViewModel.getTransactionById(newTransactionId);


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

        int counter = 0;

        copyTransaction.setAmount(transaction.getAmount());
        copyTransaction.setCategory(transaction.getCategory());
        copyTransaction.setMethod(transaction.getMethod());
        copyTransaction.setType(transaction.getType());
        copyTransaction.setTextNote(transaction.getTextNote());
        copyTransaction.setId(transaction.getId());
        copyTransaction.setDate(transaction.getDate());

        int whatToAdd;
        int howMuchToAdd;

        Log.v("RECURRING", "TRANSACTIONS ADDED: " + (counter + 1));

        return copyTransaction;
    }
}
