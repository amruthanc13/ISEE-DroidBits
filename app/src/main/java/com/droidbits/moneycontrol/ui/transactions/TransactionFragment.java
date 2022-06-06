package com.droidbits.moneycontrol.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;

import java.util.List;

public class TransactionFragment extends Fragment implements TransactionListAdapter.OnTransactionNoteListener {

    private TransactionListAdapter transactionListAdapter;
    private TransactionFilter filterBottomSheetDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_listview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.transactionListView);
        LinearLayout emptyTransactions = view.findViewById(R.id.emptyPageViewWrapper);
        TextView allTransactionsText = view.findViewById(R.id.allTransactionsText);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TransactionsViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        transactionListAdapter = new TransactionListAdapter(
                getActivity(),
                this,
                transactionViewModel,
                categoriesViewModel
        );
        recyclerView.setAdapter(transactionListAdapter);

        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transactions>>() {
            @Override
            public void onChanged(final List<Transactions> transactions) {
                transactionListAdapter.setTransactions(transactions);
                if (transactions.size() > 0) {
                    emptyTransactions.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    emptyTransactions.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        AppCompatButton addTransactionLayout = view.findViewById(R.id.addTransactionButton);

        addTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new AddTransactionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button filterButton = view.findViewById(R.id.filterTransactionButton);
        filterBottomSheetDialog = new TransactionFilter(transactionViewModel, categoriesViewModel, transactionListAdapter, recyclerView, emptyTransactions);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                filterBottomSheetDialog.show(getParentFragmentManager(), "Tag");
            }
        });

        return view;

    }

    /**
     * This method is to view the transaction detail fragment.
     * @param transaction Transaction selected transaction
     * @param position int selected position
     */
    @Override
    public void onTransactionClick(final Transactions transaction, final int position) {
        Bundle bundle = new Bundle();
        bundle.putLong("transactionDate", transaction.getDate());
        bundle.putFloat("transactionAmount", transaction.getAmount());
        bundle.putString("transactionNote", transaction.getTextNote());
        bundle.putString("transactionType", transaction.getType());
        bundle.putString("transactionCategory", transaction.getCategory());
        bundle.putString("transactionMethod", transaction.getMethod());

        //Move to transaction detail fragment
        Fragment fragment = new TransactionDetail();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
