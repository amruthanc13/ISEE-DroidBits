package com.droidbits.moneycontrol.ui.budget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.ui.budget.BudgetFragment;
import com.droidbits.moneycontrol.ui.budget.BudgetViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoryTransactionAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BudgetAdd extends BottomSheetDialogFragment {
    private BudgetViewModel budgetViewModel;
    private EditText amount;
    private EditText categorySpinner;
    private Button buttonSave;
    private CategoriesViewModel categoriesViewModel;

    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     */
    public BudgetAdd(
            final BudgetViewModel tViewModel
    ) {
        this.budgetViewModel = tViewModel;
    }

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.budget_fragment_add, container, false);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
      //  budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        amount = view.findViewById(R.id.budgetAmount);
        categorySpinner = view.findViewById(R.id.budgetCategory);
        buttonSave = view.findViewById(R.id.saveBudget);

        //Set spinner
        setBudgetCategorySpinner();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });
        return view;
    }
    private void setBudgetCategorySpinner(){
        //todo: replace getAllCategories with a new function to fetch unique categories using query
        List<Categories> categories = categoriesViewModel.getAllCategories();

        CategoryTransactionAdapter iconAdapter = new CategoryTransactionAdapter(getContext(), categories);
        iconAdapter.setListOfCategroies(categories);
        categorySpinner.setText(categories.get(0).getName());
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setAdapter(iconAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        String iconName = categories.get(which).getName();
                        categorySpinner.setText(iconName);
                    }
                });
        categorySpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });
        categorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

    }

    private void submitForm() {
        // Todo: Add validations for amount

        int category = categoriesViewModel.getSingleCategory(categorySpinner.getText().toString()).getId();
        float budgetAmount = Float.parseFloat(amount.getText().toString());
        Budget newBudget = new Budget(budgetAmount, Integer.toString(category));


        //Insert new Category in to the database
        budgetViewModel.insert(newBudget);
        this.dismiss();

        Fragment fragment = new BudgetFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Toast.makeText(getContext(), "Added new budget", Toast.LENGTH_LONG).show();
    }
}
