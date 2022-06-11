package com.droidbits.moneycontrol.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.droidbits.moneycontrol.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_settings, container,false);

            // onclick event for FAQ button
            Button FAQButton = view.findViewById(R.id.helpButton);
            FAQButton.setOnClickListener(new View.OnClickListener() {
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


            return view;
        }
}
