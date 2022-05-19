package com.droidbits.moneycontrol.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.droidbits.moneycontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TransactionFilter extends BottomSheetDialogFragment {
    private TransactionsViewModel transactionsViewModel;

    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     */
    public TransactionFilter(
            final TransactionsViewModel tViewModel
    ) {
        this.transactionsViewModel = tViewModel;
    }

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_filter, container, false);
        return view;
    }
}
