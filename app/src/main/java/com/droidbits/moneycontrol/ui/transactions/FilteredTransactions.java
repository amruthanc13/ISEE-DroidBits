package com.droidbits.moneycontrol.ui.transactions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidbits.moneycontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilteredTransactions extends Fragment {

    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_detail, container, false);

    return view;
    }
}