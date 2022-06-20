package com.droidbits.moneycontrol.db.data;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;

import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.db.transaction.TransactionsRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ImportData extends AsyncTask<String, Void, String> {

    private Application application;
    private Uri uri;

    public ImportData(Application application, Uri uri) {
        this.application = application;
        this.uri = uri;
    }

    @Override
    protected String doInBackground(String... params) {
        String path = uri.getPath();
        if(!path.substring(path.length() - 3, path.length()).equalsIgnoreCase("csv")){
            Toast.makeText(application, "FAILED!! Check file format.", Toast.LENGTH_LONG).show();
            return null;
        }
       BufferedReader reader = null;
        try {
            TransactionsRepository trxMng = new TransactionsRepository(application);
            InputStream in = application.getContentResolver().openInputStream(uri);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null){

                String row[] = line.split(",");
                Transactions tx = new Transactions();

                tx.setId(Integer.parseInt(row[0].trim()));
                tx.setAmount(Float.valueOf(row[1].trim()));
                tx.setTextNote(row[2].trim());
                tx.setType(row[3].trim());
                tx.setMethod(row[4]);
                tx.setDate(Long.valueOf(row[5].trim()));
                tx.setRepeating(Boolean.valueOf(row[6].trim()));
                tx.setRepeatingIntervalType(Integer.valueOf(row[7].trim()));
                tx.setCategory(row[8].trim());
                tx.setUserId(row[9].trim());
                trxMng.insert(tx);
            }
        }catch (NumberFormatException e){
            Toast.makeText(application, "FAILED!! Check file data.", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Toast.makeText(application, "Successfully imported data!!", Toast.LENGTH_LONG).show();
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
        File exportFile = new File(new File("//sdcard//Download//"), "transaction_export.csv");
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