package com.droidbits.moneycontrol.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.data.ExportData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // onclick event for FAQ button
        Button fAQButton = view.findViewById(R.id.helpButton);
        Button exportBtn = view.findViewById(R.id.export_button);
        Button importBtn = view.findViewById(R.id.import_button);
        fAQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new HelpMenuFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // onclick event for set defaults button
        Button defaultSetButton = view.findViewById(R.id.default_button);
        defaultSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new DefaultValuesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // onclick event for log out button
        Button logOutButton = view.findViewById(R.id.logout_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Exit Application?");
                builder.setMessage("Are you sure you want to logout and exit from finzo?");
                builder.setBackground(getContext().getDrawable(
                        (R.drawable.alert_dialogue_box)));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        getActivity().finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Toast.makeText(getActivity().getApplicationContext(), "Exporting data in progress!!",
                        Toast.LENGTH_LONG).show();
                new ExportData(getActivity().getApplication()).execute();
                Toast.makeText(getActivity().getApplicationContext(), "File exported, /sdcard//Download/transaction_export.csv!!",
                        Toast.LENGTH_LONG).show();
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Importing Data!! Plz wait..",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
