package com.droidbits.moneycontrol.ui.transactions;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.utils.CurrencyUtils;
import com.droidbits.moneycontrol.utils.DateUtils;

public class TransactionDetail extends Fragment {


    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_detail, container, false);

        CategoriesViewModel categoriesViewModel= new ViewModelProvider(this).get(CategoriesViewModel.class);
        Bundle bundle = this.getArguments();
        Long date = bundle.getLong("transactionDate");
        float amount = bundle.getFloat("transactionAmount");
        String note = bundle.getString("transactionNote");
        String type = bundle.getString("transactionType");
        String method = bundle.getString("transactionMethod");
        String category = bundle.getString("transactionCategory");
        Boolean isRepeating = bundle.getBoolean("isRepeating");
        int repeatingIntervalType = 0;
        if(isRepeating){
            repeatingIntervalType = bundle.getInt("repeatingIntervalType");
        }


        TextView transactionDate = view.findViewById(R.id.transactionDate);
        TextView transactionAmount = view.findViewById(R.id.transactionAmount);
        TextView transactionType = view.findViewById(R.id.transactionType);
        TextView transactionMethod = view.findViewById(R.id.transactionMethod);
        TextView transactionNote = view.findViewById(R.id.transactionNote);
        TextView transactionCategory = view.findViewById(R.id.transactionCategoryTitle);
        ImageView categoryImage = view.findViewById(R.id.transactionCategoryImage);
        LinearLayout repeatingIntervalLayout = view.findViewById(R.id.repeatingIntervalLayout);
        TextView repeatingInterval = view.findViewById(R.id.repeatingInterval);

        transactionType.setText(type);
        // turn float to string
        String amountToString = CurrencyUtils.formatAmount(amount);
        // transaction Type
        if (type.equals("Expense")) {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorExpense));
            amountToString = "- " + amountToString;
        } else {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIncome));
            amountToString = "+ " + amountToString;
        }

        transactionDate.setText(DateUtils.formatDate(date));
        transactionAmount.setText(amountToString);
        transactionType.setText(type);
        transactionNote.setText(note);
        transactionMethod.setText(method);
        if(repeatingIntervalType != 0){
            String[] dropdownItems = {"Daily", "Weekly", "Monthly", "Yearly"};
            repeatingIntervalLayout.setVisibility(View.VISIBLE);
            repeatingInterval.setText(dropdownItems[repeatingIntervalType-1]);
        }

        Categories categories = categoriesViewModel.getSingleCategory(Integer.parseInt(category));
        transactionCategory.setText(categories.getName());
        categoryImage.setImageResource(categories.getIcon());

        return view;
    }
}

