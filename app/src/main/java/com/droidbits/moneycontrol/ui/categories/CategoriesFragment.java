package com.droidbits.moneycontrol.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.droidbits.moneycontrol.R;

public class CategoriesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container,false);

   /*     AppCompatButton addTransactionLayout = v.findViewById(R.id.categories);

        String categoryList[] = {"Rent", "Groceries", "Phone/Internet", "Shopping", "Insurance"};
        int categoryImages[] = {R.drawable.rent, R.drawable.groceries, R.drawable.phone, R.drawable.shopping, R.drawable.insurance};
        ListView listView;
        listView = (ListView) v.findViewById(R.id.categories);
        CategoryBaseAdapter categoryBaseAdapter = new CategoryBaseAdapter(getActivity().getApplicationContext(), categoryList, categoryImages);
        listView.setAdapter(categoryBaseAdapter);*/

        return v;


    }
}
