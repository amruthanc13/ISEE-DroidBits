package com.droidbits.moneycontrol.ui.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidbits.moneycontrol.MainActivity;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.account.Account;
import com.droidbits.moneycontrol.db.budget.Budget;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.budget.BudgetViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.categories.DetailCategoryFragment;
import com.droidbits.moneycontrol.ui.settings.DefaultsViewModel;
import com.droidbits.moneycontrol.ui.transactions.AddTransactionFragment;
import com.droidbits.moneycontrol.ui.transactions.TransactionsViewModel;
import com.droidbits.moneycontrol.ui.users.UsersViewModel;
import com.droidbits.moneycontrol.utils.CurrencyUtils;
import com.droidbits.moneycontrol.utils.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String CURRENCY_DEFAULT_NAME = "Currency";

    private TextView totalIncomeText;
    private TextView totalExpenseText;
    private TextView pieChartTitle;

    private LinearLayout summaryContainer, infoTipContainer;
    private RecyclerView featuredRecycler, infoTipRecycler;
    private RecyclerView.Adapter adapter, infoAdapter;
    private MaterialAlertDialogBuilder dialogBuilder;


    private CategoriesViewModel categoriesViewModel;
    private TransactionsViewModel transactionViewModel;
    private DefaultsViewModel defaultsViewModel;
    private UsersViewModel usersViewModel;
    private AccountViewModel accountViewModel;
    private BudgetViewModel budgetViewModel;

    private CardView selectedAccountColor;
    private TextView selectedAccountTitle;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private String defaultCurrencySymbol;

    private PieChart pieChart;

    private String[] accountNames;


    @Nullable
    @Override
    public  View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // text view details
        totalIncomeText = view.findViewById(R.id.totalIncomeText);
        totalExpenseText = view.findViewById(R.id.totalExpenseText);

        //cumulative expense detials
        summaryContainer = view.findViewById(R.id.summaryContainer);
        featuredRecycler = view.findViewById(R.id.featured_recycler);
        infoTipContainer = view.findViewById(R.id.home_infoTips);
        infoTipRecycler = view.findViewById(R.id.infoTips_recycler);


        //graph details
        pieChart = view.findViewById(R.id.pieChart);

        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity().getApplication());

        //view Models
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);


        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(final List<Account> accounts) {
                accountNames = new String[accounts.size()];

                for (int i = 0; i < accounts.size(); i++) {
                    accountNames[i] = accounts.get(i).getName();
                }

                buildSelectAccountDialog(accountNames);
            }
        });

        String defaultCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);
        defaultCurrencySymbol = defaultsViewModel.getCurrencySymbol(defaultCurrency);



        //button details
        AppCompatButton addTransactionLayout = view.findViewById(R.id.addTransactionButton);
        AppCompatButton addAccountButton = view.findViewById(R.id.addAccountButton);

        //Account details
        selectedAccountTitle = view.findViewById(R.id.selectedAccountTitle);
        LinearLayout accountsWrapper = view.findViewById(R.id.accountsWrapper);
        Button switchAccount = accountsWrapper.findViewById(R.id.switchAccountButton);
        selectedAccountColor = view.findViewById(R.id.selectedAccountColor);

        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new AddAccountFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        updateSelectedAccountInformation();


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

        // cumulative expense
        featuredRecycler(view);

        //infoTips
        infoTipRecycler(view);


        return view;
    }

    @SuppressWarnings({"checkstyle", "magicnumber"})
    private void featuredRecycler(final View rView) {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocation = new ArrayList<>();

        //Daily avg
        Float dailyAvgExpense = 0F;
        Float dailyAvgIncome = 0F;

        //Monthly avg
        Float monthlyAvgExpense = 0F;
        Float monthlyAvgIncome = 0F;

        dailyAvgExpense = (float) transactionViewModel.getDailyAvg("Expense");
        dailyAvgIncome = (float) transactionViewModel.getDailyAvg("Income");

        monthlyAvgIncome = (float) transactionViewModel.getMonthlyAvg("Income");
        monthlyAvgExpense = (float) transactionViewModel.getMonthlyAvg("Expense");


        if (dailyAvgExpense != 0 || dailyAvgIncome != 0) {

            String expAmountDaily = CurrencyUtils.formatAmount(dailyAvgExpense, defaultCurrencySymbol);
            String incAmountDaily = CurrencyUtils.formatAmount(dailyAvgIncome, defaultCurrencySymbol);

            summaryContainer.setVisibility(View.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Daily Average", expAmountDaily, incAmountDaily));

        }

        if (monthlyAvgExpense != 0 || monthlyAvgIncome != 0) {


            String expAmountMonthly = CurrencyUtils.formatAmount(monthlyAvgExpense);
            String incAmountMonthly = CurrencyUtils.formatAmount(monthlyAvgIncome);

            summaryContainer.setVisibility(View.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Monthly Average", expAmountMonthly, incAmountMonthly));

        }

        adapter = new FeaturedAdapter(featuredLocation);

        featuredRecycler.setAdapter(adapter);
    }

    private void initializeTotalIncome() {
         float totalIncome = (float) transactionViewModel.getIncomeTransactionSum();

         totalIncomeText.setText(CurrencyUtils.formatAmount(totalIncome, defaultCurrencySymbol));
    }

    private void initializeTotalExpense() {
        float totalExpense = (float) transactionViewModel.getExpenseTransactionSum();
        totalExpenseText.setText(CurrencyUtils.formatAmount(totalExpense, defaultCurrencySymbol));
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

                //open categories details page on click
                int index = (int) h.getX();
                Categories cat = categoriesViewModel.getSingleCategory(xData[index]);

                Bundle bundle = new Bundle();
                bundle.putString("categoryTitle", cat.getName());
                bundle.putInt("categoryImage", cat.getIcon());
                bundle.putString("categoryId", cat.getId().toString());

                Fragment fragment = new DetailCategoryFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    /**
     * Update selected account info.
     */
    private void updateSelectedAccountInformation() {
        Account selectedAcc = accountViewModel
                .getAccountById(Integer.parseInt(sharedPreferencesUtils.getCurrentAccountIdKey()));

        int accountColor = selectedAcc.getColor();

        selectedAccountTitle.setText(selectedAcc.getName());

        if (accountColor != 0) {
            selectedAccountColor.setBackgroundResource(accountColor);

        } else {
            selectedAccountColor.setBackgroundResource(R.drawable.default_icon);

        }

    }

    @SuppressWarnings({"checkstyle", "magicnumber"})
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
    /**
     * Populate card view.
     * @param rView view
     */
    private void infoTipRecycler(final View rView) {

        infoTipRecycler.setHasFixedSize(true);
        infoTipRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<InfoTipHelperClass> infoTipLocation = new ArrayList<>();

        List<Transactions> listTransaction = transactionViewModel.getAllTransactionsForAccount();
        List<Budget> listBudget = budgetViewModel.getAllBudgetForAccount();
        List<Categories> listCategory = categoriesViewModel.getAllCategories();

        if (listBudget.isEmpty()) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Budget", "Try setting a budget to monitor your expenses"));
        } else {
            if (!listTransaction.isEmpty()) {

                for (Categories category : listCategory) {
                    String catId = category.getId().toString();
                    Budget budget = budgetViewModel.getBudgetWithCategory(catId);
                    String catName = category.getName();

                    if (budget != null) {
                        Float budgetAmount = budget.getAmount();
                        Float totalAmount = transactionViewModel.getTotalIAmountByCategoryId(catId);

                        if (budgetAmount - totalAmount < 0) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Budget Exceeded",
                                    "Budget limit exceeded for  " + catName));
                        } else if (budgetAmount - totalAmount == 0) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Budget Full",
                                    "Budget limit is full for " + catName));
                        } else if (budgetAmount - totalAmount <= 50) {
                            infoTipContainer.setVisibility(rView.VISIBLE);
                            infoTipLocation.add(new InfoTipHelperClass("Low Budget",
                                    "Monitor your expenses for " + catName));
                        }
                    }
                }
            }
        }

        ArrayList<String> defaultCategoryList = new ArrayList<String>();
        ArrayList<String> defaultCategoryListObtained = new ArrayList<String>();
        defaultCategoryList.add("Cinema");
        defaultCategoryList.add("Travel");
        defaultCategoryList.add("Shopping");
        defaultCategoryList.add("Dine out");
        defaultCategoryList.add("Bills");
        defaultCategoryList.add("Drinks");
        defaultCategoryList.add("Income");

        for (Categories category : listCategory) {
            String catName = category.getName();
            defaultCategoryListObtained.add(catName);

        }

        Collections.sort(defaultCategoryList);
        Collections.sort(defaultCategoryListObtained);

        if (defaultCategoryList.equals(defaultCategoryListObtained)) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Custom Category",
                    "Create custom categories and add transactions to it"));
        }

        String defaultCurrency = defaultsViewModel.getDefaultValue("Currency");
        String defaultPaymentMode = defaultsViewModel.getDefaultValue("Payment");
        String defaultCategory = defaultsViewModel.getDefaultValue("Category");

        if (defaultCurrency.equals("EUR") && defaultPaymentMode.equals("Credit Card") && defaultCategory.equals("Cinema")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Defaults",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultCurrency.equals("EUR")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Currency",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultPaymentMode.equals("Credit Card")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Payment Mode",
                    "Try setting defaults in settings screen to ease transaction addition"));
        } else if (defaultCategory.equals("Cinema")) {
            infoTipContainer.setVisibility(rView.VISIBLE);
            infoTipLocation.add(new InfoTipHelperClass("Set Default Category",
                    "Try setting defaults in settings screen to ease transaction addition"));
        }


        infoAdapter = new InfoTipAdapter(infoTipLocation);
        infoTipRecycler.setAdapter(infoAdapter);

    }


    /**
     * Populate account dialog.
     * @param names account names.
     */
    private void buildSelectAccountDialog(final String[] names) {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select your account:")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Account selectedAccount = accountViewModel.getAccountByName(names[which]);

                        usersViewModel.updateUserSelectedAccount(String.valueOf(selectedAccount.getId()));
                        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(selectedAccount.getId()));

                        /*updateSelectedAccountInformation();
                        calculateAccountBalances();*/

                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                    }
                });
    }
}
