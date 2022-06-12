package com.droidbits.moneycontrol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.categories.CategoriesRepository;
import com.droidbits.moneycontrol.db.transaction.TransactionsRepository;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.transactions.AddTransactionFragment;
import com.droidbits.moneycontrol.ui.transactions.TransactionsViewModel;
import com.droidbits.moneycontrol.utils.CurrencyUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private TextView totalIncomeText, totalExpenseText, pieChartTitle;

    private CategoriesViewModel categoriesViewModel;
    private TransactionsViewModel transactionViewModel;

    private PieChart pieChart;

    @Nullable
    @Override
    public  View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // text view details
        totalIncomeText = view.findViewById(R.id.totalIncomeText);
        totalExpenseText = view.findViewById(R.id.totalExpenseText);

        //graph details
        pieChart = view.findViewById(R.id.pieChart);

        //view Models
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        //button details
        AppCompatButton addTransactionLayout = view.findViewById(R.id.addTransactionButton);


        // Add transaction button
        addTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new AddTransactionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // set total income
        initializeTotalIncome();

        // set total expense
        initializeTotalExpense();

        // Pie Chart
        initializePieChart(view);

        return view;
    }

    private void initializeTotalIncome() {
         float totalIncome = (float) transactionViewModel.getIncomeTransactionSum();

         totalIncomeText.setText(CurrencyUtils.formatAmount(totalIncome));
    }

    private void initializeTotalExpense() {
        float totalExpense = (float) transactionViewModel.getExpenseTransactionSum();

        totalExpenseText.setText(CurrencyUtils.formatAmount(totalExpense));
    }

    private void initializePieChart(final View view) {

        String[] xDataS = categoriesViewModel.getCategoriesName();
        int length = 0;
        for (String nameCat : xDataS) {
            Categories catCategory = categoriesViewModel.getSingleCategory(nameCat);
            catCategory.getId();
            if (transactionViewModel.getTotalIAmountByCategoryId(catCategory.getId().toString()) > 0f) {
                length++;
            }
        }

        LinearLayout pieLayout = view.findViewById(R.id.pieChartContainer);

        pieChartTitle = view.findViewById(R.id.pieChartTitle);
        pieChartTitle.setText("Expense by Categories:");


        if (length == 0) {
            pieLayout.setVisibility(view.GONE);
        } else {
            pieLayout.setVisibility(view.VISIBLE);
        }
        float[] yData = new float[length];
        String[] xData = new String[length];
        int i = 0;
        for (String name : xDataS) {
            Categories cat = categoriesViewModel.getSingleCategory(name);
            if (transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString()) > 0f) {
                yData[i] = transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString());
                xData[i] = name;
                i++;
            }
        }

        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        //pieChart.setHoleRadius(0f);
        pieChart.setDescription(null);
        pieChart.setTransparentCircleAlpha(0);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        System.out.println(yData);
        System.out.println(xData);
        addDataSet(yData, xData);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, final Highlight h) {

                int index = (int) h.getX();
                Categories cat = categoriesViewModel.getSingleCategory(xData[index]);

                Bundle bundle = new Bundle();
                bundle.putString("categoryTitle", cat.getName());
                bundle.putInt("categoryImage", cat.getIcon());
                bundle.putInt("categoryId", cat.getId());
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void addDataSet(final float[] yData, final String[] xData) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for (int i = 1; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.projectColorOrange));
        colors.add(getResources().getColor(R.color.projectColorPurple));
        colors.add(getResources().getColor(R.color.projectColorGreen));
        colors.add(getResources().getColor(R.color.projectColorDarkBlue));
        colors.add(getResources().getColor(R.color.projectColorDarkOrange));
        colors.add(getResources().getColor(R.color.colorExpense));
        colors.add(getResources().getColor(R.color.projectColorDarkGreen));
        colors.add(getResources().getColor(R.color.projectColorDarkPurple));
        colors.add(getResources().getColor(R.color.projectColorDefault));

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}
