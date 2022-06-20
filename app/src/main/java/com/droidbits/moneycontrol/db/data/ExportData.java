package com.droidbits.moneycontrol.db.data;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;

import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsRepository;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ExportData extends AsyncTask<String, Void, String> {

        private Application application;

        public ExportData(Application application) {
                this.application = application;
        }

        @Override
        protected String doInBackground(String... params) {
                TransactionsRepository trx = new TransactionsRepository(application);
                List<Transactions> exportData = trx.getAllTransactionsExportData();
                writeInFile(exportData);
                return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        private void writeInFile(List<Transactions> list){
                final String DELIMITER = ",";
                final String SEPARATOR = "\n";
                final String HEADER = "id," +
                        "amount," +
                        "text_note," +
                        "type," +
                        "method," +
                        "date," +
                        "is_repeating," +
                        "repeating_interval_type," +
                        "category," +
                        "user_id";

                FileWriter fileWriter = null;
                //new File("//sdcard//Download//")
                File exportFile = new File(Environment.getExternalStorageDirectory(), "//Download//transaction_export.csv");
                try {
                        fileWriter = new FileWriter(exportFile);
                        //Add header
                        fileWriter.append(HEADER);
                        //Add a new line after the header
                        fileWriter.append(SEPARATOR);
                        //Iterate through bookList
                        for (Transactions tx: list)  {
                                fileWriter.append(tx.getId()+"");
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getAmount()+"");
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getTextNote());
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getType());
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getMethod());
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getDate()+"");
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getIsRepeating()+"");
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getRepeatingIntervalType()+"");
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getCategory());
                                fileWriter.append(DELIMITER);
                                fileWriter.append(tx.getUserId());
                                fileWriter.append(DELIMITER);

                                fileWriter.append(SEPARATOR);
                        }
                        DownloadManager downloadManager = (DownloadManager) application.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.addCompletedDownload(exportFile.getName(),
                                exportFile.getName(), true,
                                "text/plain", exportFile.getAbsolutePath(),
                                exportFile.length(),true);
                        fileWriter.close();
                }
                catch(Exception e) {
                        e.printStackTrace();
                }
        }
    }