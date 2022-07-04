package com.droidbits.moneycontrol.ui.budget;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.budget.BudgetRepository;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {
    private BudgetRepository budgetRepository;

    public BudgetViewModel(Application application) {
        super(application);
        budgetRepository = new BudgetRepository(application);
    }
    public void insert(Budget budget) {
        budgetRepository.insert(budget);
    }
    public LiveData<List<Budget>> getAllBudget() {
        return budgetRepository.getAllBudget();
    }
    public Budget getBudgetWithCategory(final String category) {
        return budgetRepository.getBudgetWithCategory(category);
    }
    public Budget getSingleBudget(final int id) {
        return budgetRepository.getSingleBudget(id);
    }
    public double getBudgetAmountByCategory(String categoryId) {
        return budgetRepository.getBudgetAmountByCategory(categoryId);
    }
    public List<Budget> getAllBudgetForAccount() {
        return budgetRepository.getAllBudgetForAccount();
    }


    public void updateBudgetAmountsDefaultCurrency(float exchangeRate) {
        budgetRepository.updateBudgetAmountsDefaultCurrency(exchangeRate);
    }
}
