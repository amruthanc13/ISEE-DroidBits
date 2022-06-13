package com.droidbits.moneycontrol.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnCategoryNoteListener {

    private CategoriesAdapter adapter;
    private AddCategory  categoryBottomSheetDialog;

    @Nullable
    @Override
    @SuppressWarnings({"checkstyle", "magicnumber"})
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.categories_grid_view);

        adapter = new CategoriesAdapter(getActivity(), this);
        //Set the adapter to gridView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        final CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        adapter.setCategories(categoriesViewModel.getAllCategories());

        AppCompatButton addCategory = view.findViewById(R.id.addCategories);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new AddCategory(categoriesViewModel);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

               // categoryBottomSheetDialog.show(getParentFragmentManager(), "Tag");
            }
        });

    return view;
    }

    @Override
    public void onCategoryClick(Categories categories, int position) {

    }
}
