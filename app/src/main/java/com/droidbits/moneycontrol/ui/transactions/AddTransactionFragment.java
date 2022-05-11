package com.droidbits.moneycontrol.ui.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.utils.DateUtils;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;


public class AddTransactionFragment extends Fragment{
    private EditText tiedtTransactionAmount, tiedtTransactionNote;
    private EditText textCategory;
    private String transactionType;
    private String paymentMethod;
    private Long transactionDate;
    private TransactionsViewModel transactionViewModel;
    private View currentView;
    private Button btnSave;

    private static DecimalFormat df = new DecimalFormat("#.00");

    private EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_transactions, container,false);
        Spinner myspinner = v.findViewById((R.id.spinnerTransactionType));
        ArrayAdapter myadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.transaction_type));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myadapter);

        Spinner myspinner2 = v.findViewById((R.id.spinnerPaymentMethod));
        ArrayAdapter myadapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.payment_method));
        myadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner2.setAdapter(myadapter2);

        etDate = v.findViewById(R.id.transactionDate);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

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
        transactionType = ((Spinner)view.findViewById(R.id.spinnerTransactionType)).getSelectedItem().toString();
        paymentMethod = ((Spinner)view.findViewById(R.id.spinnerPaymentMethod)).getSelectedItem().toString();;


        transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        btnSave = view.findViewById(R.id.saveTransaction);

        etDate = view.findViewById(R.id.transactionDate);
        etDate.setText(DateUtils.formatDate(DateUtils.getStartOfCurrentDay().getTimeInMillis()));
        transactionDate = DateUtils.getStartOfCurrentDay().getTimeInMillis();



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
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        if (!checkTransactionAmount()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }

        Log.d("String Text", "Value:" + paymentMethod);

        float transactionAmount = Float.parseFloat(tiedtTransactionAmount.getText().toString());
        String transactionNote  = tiedtTransactionNote.getText().toString().trim() + "";
        Transactions newTransaction = new Transactions((float) transactionAmount,
                transactionNote,
                transactionType,
                paymentMethod,
                DateUtils.getStartOfDayInMS(transactionDate),
                ""
        );


        //Insert new Category in to the database
        long newTransactionId = transactionViewModel.insert(newTransaction);
        Log.d("newTransactionId", "Value:" + newTransactionId);

        Transactions insertedTransaction = transactionViewModel.getTransactionById(newTransactionId);


        Fragment fragment = new TransactionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        String total = "Added new transaction";
        Toast.makeText(getContext(), total, Toast.LENGTH_LONG).show();

}


}