package com.droidbits.moneycontrol.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;

import java.util.List;

public class BudgetFragment extends Fragment implements BudgetAdapter.OnBudgetNoteListener {
    private Button buttonBudget;
    private BudgetAdd budgetBottomSheetDialog;
    private BudgetAdapter adapter;


    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_budget, container, false);
        Button buttonBudget = view.findViewById(R.id.addBudget);
        RecyclerView recyclerView = view.findViewById(R.id.budgetGridView);
        final BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetBottomSheetDialog = new BudgetAdd(budgetViewModel);
        final CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        adapter = new BudgetAdapter(getActivity(), this, categoriesViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        budgetViewModel.getAllBudget().observe(getViewLifecycleOwner(), new Observer<List<Budget>>() {
            @Override
            public void onChanged(final List<Budget> budgets) {
                adapter.setBudgets(budgets);
                if (budgets.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        buttonBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                budgetBottomSheetDialog.show(getParentFragmentManager(), "Tag");
            }
        });
        return view;

}

    @Override
    public void onBudgetClick(Budget budget, int position) {

    }
}
