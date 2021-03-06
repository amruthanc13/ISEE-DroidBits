package com.droidbits.moneycontrol.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droidbits.moneycontrol.R;

public class HelpMenuFragment extends Fragment {

    private ExpandableListView expandableTextView;

    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inflater,
            final  @Nullable ViewGroup container,
            final  @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        expandableTextView = view.findViewById(R.id.expandable_list_view);
        FAQAdapter faqAdapter = new FAQAdapter(getContext());
        expandableTextView.setAdapter(faqAdapter);

        return view;
    }
}
