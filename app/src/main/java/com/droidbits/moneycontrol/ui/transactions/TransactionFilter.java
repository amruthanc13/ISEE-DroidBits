package com.droidbits.moneycontrol.ui.transactions;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.droidbits.moneycontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class TransactionFilter extends BottomSheetDialogFragment {
    private TransactionsViewModel transactionsViewModel;
    private String categoriesFilter;
    private String paymentMethodFilter;
    private Long transactionFromDate;
    private Long transactionToDate;
    private EditText fromDate;
    private EditText toDate;
    private Button filterButton;
    DatePickerDialog.OnDateSetListener setListener;
    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     */
    public TransactionFilter(
            final TransactionsViewModel tViewModel
    ) {
        this.transactionsViewModel = tViewModel;
    }

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_filter, container, false);
        Spinner mySpinner = view.findViewById((R.id.spinnerCategories));
        ArrayAdapter myadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories_filter));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myadapter);

        Spinner mySpinnerPM = view.findViewById((R.id.spinnerPayment));
        ArrayAdapter myAdapterPM = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.payment_filter));
        myAdapterPM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinnerPM.setAdapter(myAdapterPM);

        fromDate = view.findViewById(R.id.transactionFromDate);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        fromDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        toDate = view.findViewById(R.id.transactionToDate);
        Calendar calendarTo = Calendar.getInstance();
        final int yearTo = calendar.get(Calendar.YEAR);
        final int monthTo = calendar.get(Calendar.MONTH);
        final int dayTo = calendar.get(Calendar.DAY_OF_MONTH);

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        toDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        /*
        // Button to FilteredTransactions
        filterButton = (Button) filterButton.findViewById(R.id.applyFiltersTransaction);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilteredTransactions();
            }
        });*/
        return view;
    }/*
    public void openFilteredTransactions(){
        Intent filterIntent = new Intent(getActivity(), FilteredTransactions.class);
        startActivity(filterIntent);
    }*/

    }

