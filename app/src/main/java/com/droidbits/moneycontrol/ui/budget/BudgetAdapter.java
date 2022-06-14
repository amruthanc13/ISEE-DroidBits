package com.droidbits.moneycontrol.ui.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.settings.DefaultsViewModel;
import com.droidbits.moneycontrol.utils.CurrencyUtils;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private static final String CURRENCY_DEFAULT_NAME = "Currency";
    private List<Budget> budgets;
    private final LayoutInflater layoutInflater;
    private static final String TAG = "CategoryAdapter";
    private OnBudgetNoteListener mOnNoteListener;
    private final CategoriesViewModel categoriesViewModel;
    private final DefaultsViewModel defaultsViewModel;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public BudgetAdapter(final @NonNull Context context, OnBudgetNoteListener mOnNoteListener, CategoriesViewModel categoriesViewModel, DefaultsViewModel defaultsViewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mOnNoteListener = mOnNoteListener;
        this.categoriesViewModel = categoriesViewModel;
        this.defaultsViewModel = defaultsViewModel;
    }

    @NonNull
    @Override
    public BudgetAdapter.BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.budget_list_item, parent, false);
        return new BudgetViewHolder(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.BudgetViewHolder holder, int position) {
        if (budgets != null) {
            Budget current =  budgets.get(position);
           // Context context = holder.budget.getContext();
            String defaultCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);
            String defaultCurrencySymbol = defaultsViewModel.getCurrencySymbol(defaultCurrency);
            Float amount = current.getAmount();
            String category = current.getCategory();
            String amountToString = CurrencyUtils.formatAmount(amount, defaultCurrencySymbol);
            holder.budgetAmount.setText(amountToString);
            Categories categories = categoriesViewModel.getSingleCategory(Integer.parseInt(category));
            holder.categoryImage.setImageResource(categories.getIcon());
            holder.categoryTitle.setText(categories.getName());
            viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(current.getId()));
        }
    }
    public void setBudgets(final List<Budget> budgets) {
        this.budgets = budgets;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (budgets != null) {
            return budgets.size();
        } else {
            return 0;
        }
    }

    final class BudgetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView categoryImage;
        private final TextView categoryTitle;
        private final TextView budgetAmount;
        private final OnBudgetNoteListener onNoteListener;
        private final SwipeRevealLayout swipeRevealLayout;


        /**
         * Transaction view holder.
         * @param itemView view that will hold the category list.
         * @param onBudgetNoteListener OnNoteListener
         */
        private BudgetViewHolder(final View itemView, final OnBudgetNoteListener onBudgetNoteListener) {
            super(itemView);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onNoteListener.onBudgetClick(budgets.get(getAdapterPosition()), getAdapterPosition());
                }
            };
            categoryImage = itemView.findViewById(R.id.transactionCategoryImage);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            this.onNoteListener = onBudgetNoteListener;
            budgetAmount = itemView.findViewById(R.id.budgetAmount);

            itemView.setOnClickListener(this);
        }

        /**
         * method to get catch onclick event of grid adapter.
         * @param v view
         */
        @Override
        public void onClick(final View v) {
            onNoteListener.onBudgetClick(budgets.get(getAdapterPosition()), getAdapterPosition());
        }
    }
    public interface OnBudgetNoteListener {
        /**
         * This method is to get the transaction and position of selected transaction.
         * @param budget Transaction selected transaction
         * @param position int selected position
         */
        void onBudgetClick(Budget budget, int position);
    }
}
