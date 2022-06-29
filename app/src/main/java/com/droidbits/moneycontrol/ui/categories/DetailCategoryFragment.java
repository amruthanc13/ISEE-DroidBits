package com.droidbits.moneycontrol.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.settings.DefaultsViewModel;
import com.droidbits.moneycontrol.ui.transactions.TransactionDetail;
import com.droidbits.moneycontrol.ui.transactions.TransactionListAdapter;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.ui.transactions.TransactionsViewModel;

import java.util.List;
import androidx.lifecycle.Observer;


public class DetailCategoryFragment extends Fragment implements TransactionListAdapter.OnTransactionNoteListener {
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.category_detail, container, false);

        Bundle bundle = this.getArguments();
        String title = bundle.getString("categoryTitle");
        int image = bundle.getInt("categoryImage");
        String categoryId = bundle.getString("categoryId");

        TextView categoryTitle = view.findViewById(R.id.categoryTitle);
        ImageView categoryImage = view.findViewById(R.id.categoryImage);

        categoryTitle.setText(title);
        categoryImage.setImageResource(image);

        TransactionsViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        CategoriesViewModel categoryViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        DefaultsViewModel defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        //Create the adapter for Transaction
        final TransactionListAdapter adapter = new TransactionListAdapter(
                getActivity(),
                this,
                transactionViewModel,
                categoryViewModel,
                defaultsViewModel
        );
        RecyclerView recyclerView = view.findViewById(R.id.transactionListView);
        LinearLayout emptyListImage = view.findViewById(R.id.emptyPageViewWrapper);

        transactionViewModel.getTransactionsForCategory(categoryId).observe(getViewLifecycleOwner(), new Observer<List<Transactions>>() {
            @Override
            public void onChanged(final List<Transactions> transactions) {
                adapter.setTransactions(transactions);

                if (transactions.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyListImage.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyListImage.setVisibility(View.VISIBLE);
                }
            }
        });

        //Set the adapter to list view
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionViewModel.getTransactionsForCategory(categoryId);

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
