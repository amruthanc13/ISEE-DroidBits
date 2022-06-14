package com.droidbits.moneycontrol.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.MoneyControlDB;
import com.droidbits.moneycontrol.db.currency.CurrencyDao;
import com.droidbits.moneycontrol.ui.budget.BudgetViewModel;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.ui.transactions.TransactionsViewModel;
import com.droidbits.moneycontrol.utils.FormatterUtils;
import com.droidbits.moneycontrol.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class DefaultValuesFragment extends Fragment {

    private static final String CURRENCY_DEFAULT_NAME = "Currency";
    private static final String CATEGORY_DEFAULT_NAME = "Category";
    private static final String PAYMENT_METHOD_DEFAULT_NAME = "Payment";

    private TextInputEditText defaultCategorySpinner;
    private TextInputEditText defaultCurrencySpinner;
    private TextInputEditText defaultPaymentSpinner;
    private Float exchangeRate;

    private CurrencyDao currencyDao;
    private DefaultsViewModel defaultsViewModel;
    private TransactionsViewModel transactionsViewModel;
    private BudgetViewModel budgetViewModel;
    private CategoriesViewModel categoriesViewModel;

    private RequestQueue requestQueue;


    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inflater,
            final  @Nullable ViewGroup container,
            final  @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deaful_values, container, false);

        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        requestQueue = Volley.newRequestQueue(getContext());

        // Set Default currency
        currencyDao = MoneyControlDB.getInstance(getContext()).currencyDao();
        defaultCurrencySpinner = view.findViewById(R.id.default_currency_spinner);
        setDefaultCurrencySpinner();

        //Set Default Category
        defaultCategorySpinner = view.findViewById(R.id.default_category_spinner);
        setDefaultCategorySpinner();

        //Set Default Payment mode
        defaultPaymentSpinner = view.findViewById(R.id.default_payment_spinner);
        setDefaultPaymentModeSpinner();

        return view;
    }

    /**
     * This method to set the default currency.
     */
    private void setDefaultCurrencySpinner() {
        String[] dropdownItems = currencyDao.getAllCurrencyCodes();

        //set currency spinner value

        String stringCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);
        defaultCurrencySpinner.setText(stringCurrency);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction currency")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        updateDefaultCurrency(dropdownItems[which]);
                    }
                });

        defaultCurrencySpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultCurrencySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultCurrencySpinner.setInputType(0);
    }

    private void setDefaultCategorySpinner() {
        String[] dropdownItems = categoriesViewModel.getCategoriesName();

        //set payment mode spinner value
        String stringCategory = defaultsViewModel.getDefaultValue(CATEGORY_DEFAULT_NAME);
        defaultCategorySpinner.setText(stringCategory);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        defaultsViewModel.updateDefaultValue(CATEGORY_DEFAULT_NAME, dropdownItems[which]);
                        defaultCategorySpinner.setText(dropdownItems[which]);
                    }
                });

        defaultCategorySpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultCategorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultCategorySpinner.setInputType(0);
    }

    private void setDefaultPaymentModeSpinner() {
        String[] dropdownItems = new String[]{"Credit Card", "Debit Card", "Cash", "Paypal", "Other"};

        //set payment mode spinner value
        String stringPayment = defaultsViewModel.getDefaultValue(PAYMENT_METHOD_DEFAULT_NAME);
        defaultPaymentSpinner.setText(stringPayment);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the payment mode")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        defaultsViewModel.updateDefaultValue(PAYMENT_METHOD_DEFAULT_NAME, dropdownItems[which]);
                        defaultPaymentSpinner.setText(dropdownItems[which]);
                    }
                });

        defaultPaymentSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultPaymentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultPaymentSpinner.setInputType(0);
    }

    /**
     * Method to update default currency.
     * @param newCurrency new currency.
     */
    private void updateDefaultCurrency(final String newCurrency) {
        if (!NetworkUtils.isNetworkConnectionAvailable(getContext())) {
            Toast.makeText(
                    getContext(),
                    "No network connectivity, can't change the default currency!",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        requestExchangeRate(newCurrency);
    }

    private void requestExchangeRate(final String targetCurrency) {
        String originalCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);

        MaterialAlertDialogBuilder exchangeRateDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Update default currency?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Toast.makeText(getContext(), "Update of default currency cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (!originalCurrency.equals(targetCurrency)) {
                           // exchangeRate = getExchangeRate(originalCurrency,targetCurrency);
                            if (exchangeRate != null) {
                                defaultCurrencySpinner.setText(targetCurrency);
                                defaultsViewModel.updateDefaultValue(CURRENCY_DEFAULT_NAME, targetCurrency);

                                transactionsViewModel.updateTransactionAmountsDefaultCurrency(exchangeRate);
                                budgetViewModel.updateBudgetAmountsDefaultCurrency(exchangeRate);
                            }
                        }
                    }
                });

        String requestUrl = "https://api.exchangerate.host/latest?base="
                + originalCurrency
                + "&symbols="
                + targetCurrency;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            exchangeRate = Float.parseFloat(
                                    response.getJSONObject("rates").get(targetCurrency).toString()
                            );
                             exchangeRateDialogBuilder
                                     .setMessage("Exchange rate: 1 "
                                             + originalCurrency
                                             + " = "
                                             + FormatterUtils.roundToFourDecimals(exchangeRate)
                                             + " "
                                             + targetCurrency
                                     ).show();
                        } catch (final JSONException e) {
                            System.out.println(e);
                            Toast.makeText(
                                    getContext(),
                                    "Something went wrong, please try again!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                // TODO: Handle error
                Toast.makeText(
                        getContext(),
                        "Something went wrong, please try again!",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
