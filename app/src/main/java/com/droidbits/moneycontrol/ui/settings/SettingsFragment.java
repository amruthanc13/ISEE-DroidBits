package com.droidbits.moneycontrol.ui.settings;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.droidbits.moneycontrol.MainActivity;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.data.ExportData;
import com.droidbits.moneycontrol.db.data.ImportData;
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
        Button referBtn = view.findViewById(R.id.refer_button);
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

        referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, 114);
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
                chooseFile1();
            }
        });
        return view;
    }


    private void chooseFile1() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                102
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 114 || resultCode == 114) {
            Uri contactUri = data.getData();
            Cursor cursor = getContext().getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int mobileCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            sendMsg( cursor.getString(mobileCol),  cursor.getString(nameCol));
        }
        if(requestCode == 102 || resultCode == 102){//102
            Toast.makeText(getActivity().getApplicationContext(),
                    "Importing Data!! Plz wait..",Toast.LENGTH_SHORT).show();
            String path = data.getData().getPath();
            new ImportData(getActivity().getApplication(), data.getData()).execute();
        }
    }

    protected void sendMsg(String mobile1, String name1) {
        Toast.makeText(getActivity(),
                "SMS sending to mobile: " + mobile1,
                Toast.LENGTH_SHORT).show();
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , mobile1);
        smsIntent.putExtra("sms_body"  , "Hi "+name1+" Please download this app from Play Store!");

        try {
            startActivity(smsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "SMS sending failed, please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

}

