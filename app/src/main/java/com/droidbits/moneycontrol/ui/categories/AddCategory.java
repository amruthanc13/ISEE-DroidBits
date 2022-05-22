package com.droidbits.moneycontrol.ui.categories;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddCategory extends BottomSheetDialogFragment {
    private CategoriesViewModel categoriesViewModel;
    private EditText categoryName;
    private EditText dropdown;
    private String categoryIconImage;


    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     */
    public AddCategory(
            final CategoriesViewModel tViewModel
    ) {
        this.categoriesViewModel = tViewModel;
    }

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.categories_add, container, false);

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoryName = view.findViewById(R.id.categoryName);
        Button addCategory = view.findViewById(R.id.addCategory);

        //Set spinner
        setCategoryIconSpinner(view);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });
        return view;
    }

    private void setCategoryIconSpinner(final View view) {
        dropdown = view.findViewById(R.id.spinnerIcon);
        //create a list of items for the spinner.
        String[] dropdownItems = getIconsFromDrawable();
        CategoryIconAdapter iconAdapter = new CategoryIconAdapter(getContext(), dropdownItems);
        iconAdapter.setListOfIcons(dropdownItems);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setAdapter(iconAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        String icon = dropdownItems[which];
                        categoryIconImage = icon;
                        dropdown.setText(icon);
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(final DialogInterface dialog) {
                        if (dropdown.getText() == null) {
                            categoryIconImage = "general";
                            dropdown.setText("general");
                        }
                    }
                });

        dropdown.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

    }

    /**
     * This method is to retrieve all the category Icons from drawable folder.
     * @return String[] category icons
     */
    public static String[] getIconsFromDrawable() {
        Field[] idFields = R.drawable.class.getFields();
        List<String> listIconArray = new ArrayList<>();
        for (int i = 0; i < idFields.length; i++) {
            try {
                if (idFields[i].getName().contains("icon_")) {
                    listIconArray.add(idFields[i].getName().split("icon_")[1]);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        String[] stringIconArray = new String[listIconArray.size()];
        stringIconArray = listIconArray.toArray(stringIconArray);
        return stringIconArray;

    }

    private void submitForm() {

        String categoryIcon = "icon_" + categoryIconImage;
        int resID = this.getResources().getIdentifier(categoryIcon, "drawable", getContext().getPackageName());
        String name = categoryName.getText().toString().trim() + "";
        Categories newCategory = new Categories(name, resID);


        //Insert new Category in to the database
        categoriesViewModel.insert(newCategory);
        this.dismiss();

        Fragment fragment = new CategoriesFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Toast.makeText(getContext(), "Added new category", Toast.LENGTH_LONG).show();
    }
}
