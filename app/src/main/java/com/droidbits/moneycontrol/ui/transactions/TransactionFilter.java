package com.droidbits.moneycontrol.ui.transactions;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoryTransactionAdapter;
import com.droidbits.moneycontrol.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionFilter extends BottomSheetDialogFragment {
    private TransactionsViewModel transactionsViewModel;
    private CategoriesViewModel categoriesViewModel ;
    RecyclerView recyclerView ;
    LinearLayout emptyTransactions;
    private EditText categoryFilterSpinner;
    private Spinner paymentFilterSpinner;
    private EditText fromDate;
    private EditText toDate;
    private String paymentFilter;
    private Long dateFrom, dateTo;
    private EditText fromAmount, toAmount;
    private Float amountFrom, amountTo;
    private Button filterButton;
    private TransactionListAdapter transactionsListAdapter;
    DatePickerDialog.OnDateSetListener setListener;
    private String categoryId;

    private final Calendar calendarFromDate = Calendar.getInstance(), calendarToDate = Calendar.getInstance(), calendarTemp = Calendar.getInstance();
    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     */
    public TransactionFilter(
            final TransactionsViewModel tViewModel,
            final CategoriesViewModel cViewModel,
            final TransactionListAdapter tAdapter,
            final RecyclerView recyclerView,
            final LinearLayout emptyTransactions
    ) {
        this.transactionsViewModel = tViewModel;
        this.categoriesViewModel = cViewModel;
        this.transactionsListAdapter = tAdapter;
        this.recyclerView = recyclerView;
        this.emptyTransactions = emptyTransactions;
    }

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_filter, container, false);
        categoryFilterSpinner = view.findViewById((R.id.spinnerCategories));
        setCategoryFilterSpinner();

        paymentFilterSpinner = view.findViewById((R.id.spinnerPayment));
        ArrayAdapter paymentFilterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.payment_filter));
        paymentFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentFilterSpinner.setAdapter(paymentFilterAdapter);

        fromDate = view.findViewById(R.id.transactionFromDate);

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
                    final DatePicker view,
                    final int year,
                    final int monthOfYear,
                    final int dayOfMonth
            ) {
                // TODO Auto-generated method stub
                calendarFromDate.set(Calendar.YEAR, year);
                calendarFromDate.set(Calendar.MONTH, monthOfYear);
                calendarFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(fromDate, calendarFromDate);
            }

        };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(
                        getContext(),
                        fromDateListener,
                        calendarFromDate.get(Calendar.YEAR),
                        calendarFromDate.get(Calendar.MONTH),
                        calendarFromDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        toDate = view.findViewById(R.id.transactionToDate);

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
                    final DatePicker view,
                    final int year,
                    final int monthOfYear,
                    final int dayOfMonth
            ) {
                // TODO Auto-generated method stub
                calendarToDate.set(Calendar.YEAR, year);
                calendarToDate.set(Calendar.MONTH, monthOfYear);
                calendarToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(toDate, calendarToDate);
            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(
                        getContext(),
                        toDateListener,
                        calendarToDate.get(Calendar.YEAR),
                        calendarToDate.get(Calendar.MONTH),
                        calendarToDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        //Set Amount fields
        fromAmount = view.findViewById(R.id.transactionFromAmount);
        toAmount = view.findViewById(R.id.transactionToAmount);



        filterButton = view.findViewById(R.id.applyFiltersTransaction);

        fromDate = view.findViewById(R.id.transactionFromDate);
        toDate = view.findViewById(R.id.transactionToDate);


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                applyFilter();
                dismissBottomSheet();
            }
        });
        return view;

    }

    /**
     * Helper method to dismiss current bottom sheet.
     */
    private void dismissBottomSheet() {
        this.dismiss();
    }

    /**
     * Apply filter conditions
     */
    private void applyFilter(){
        paymentFilter = paymentFilterSpinner.getSelectedItem().toString();

        if (!fromAmount.getText().toString().isEmpty()){
            amountFrom = Float.parseFloat(fromAmount.getText().toString());
        }else{
            amountFrom = Float.MIN_VALUE;
        }

        if (!toAmount.getText().toString().isEmpty()){
            amountTo = Float.parseFloat(toAmount.getText().toString());
        }else{
            amountTo = Float.MAX_VALUE;
        }

        if (!fromDate.getText().toString().isEmpty()){
            dateFrom = DateUtils.getStartOfDayInMS(calendarFromDate.getTimeInMillis());
            System.out.println(dateFrom);
        }
        else{
            calendarTemp.set(1700,1,1);
            dateFrom = DateUtils.getStartOfDayInMS(calendarTemp.getTimeInMillis());

        }

        if (!toDate.getText().toString().isEmpty()){
            dateTo = DateUtils.getStartOfDayInMS(calendarToDate.getTimeInMillis());
        }
        else{
            calendarTemp.set(4000,1,1);
            dateTo = DateUtils.getStartOfDayInMS(calendarTemp.getTimeInMillis());
        }

        if (!paymentFilter.isEmpty()){
            if(paymentFilter.equals("Select All")){
                paymentFilter = null;
            }
        }

        List<Transactions> transactions = transactionsViewModel.filterTransactions(amountFrom, amountTo, dateFrom, dateTo, paymentFilter, categoryId);
        transactionsListAdapter.setTransactions(transactions);
        if (transactions.size() > 0) {
            emptyTransactions.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyTransactions.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    /**
     * Helper method to build the category filter list and add a default "Select all" pseudo category.
     * @return category list.
     */
    private List<Categories> buildCategoryFilterList() {
        List<Categories> categoryFilterList = new ArrayList<>();
        Categories noCategory = new Categories(-1, "Select all", R.drawable.all);
        List<Categories> categories = categoriesViewModel.getAllCategories();

       categoryFilterList.add(noCategory);
        categoryFilterList.addAll(categories);

        return categoryFilterList;
    }


    private void setCategoryFilterSpinner(){
        List<Categories> categories = buildCategoryFilterList();

        CategoryTransactionAdapter iconAdapter = new CategoryTransactionAdapter(getContext(), categories);
        iconAdapter.setListOfCategroies(categories);
        categoryFilterSpinner.setText(categories.get(0).getName());
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setAdapter(iconAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        String iconName = categories.get(which).getName();
                        if(categories.get(which).getId() != -1){
                            categoryId = Integer.toString(categories.get(which).getId());
                        }else{
                            categoryId = null;
                        }
                        categoryFilterSpinner.setText(iconName);
                    }
                });
        categoryFilterSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });
        categoryFilterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

    }

    private void updateTransactionDateLabel(final EditText editTextLayout, final Calendar myCalendar) {
        String myFormat = "dd-MM-yy";
        editTextLayout.setText(DateUtils.formatDate(myCalendar.getTimeInMillis(), myFormat));
    }

    }
