package com.myaqi.myaqi4;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.utils.EntryXComparator;
import com.myaqi.myaqi4.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{


    TextView currentIndoorAQI, currentIndoorDesc;
    //public static float indoorCurrentAQI;
    ScatterChart scatterChart;
    Button logoutbtn;

    FirebaseAuth fAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(getApplicationContext(), Register.class));
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentIndoorDesc = findViewById(R.id.currentIndoorDesc);
        currentIndoorAQI = findViewById(R.id.currentIndoorAQI);
        logoutbtn = (Button) findViewById(R.id.logout);
        TextView OutdoorTitle = findViewById(R.id.IndoorAQI);
        TextView furtherInformation = findViewById(R.id.textView16);
        OutdoorTitle.setText("INDOOR AIR QUALITY");
        fAuth = FirebaseAuth.getInstance();
        scatterChart = findViewById(R.id.indoorScatterplot);
        IndoorDataApiCall IndoorDataApiCall = new IndoorDataApiCall();
        IndoorDataApiCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //IndoorDataApiCall.execute();

        //int[] averages = average(data);
        //updateDisplay();

        furtherInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FurtherIndoorInformation.class));
            }
        });

    }



   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case
                    R.id.outdoor_air:

                startActivity(new Intent(getApplicationContext(), OutdoorAir.class));
                break;


            case
                    R.id.logout:
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
            case
                    R.id.switch_device:
                startActivity(new Intent(getApplicationContext(), SetupDevice.class));
                break;

            default:
                break;

        }
        return true;
    }

    private class IndoorDataApiCall extends AsyncTask<Void, Void, int[]> {

        private TextView hourlyAQI, dailyAQI;

        @Override
        protected int[] doInBackground(Void... voids) {

            List<Entry> entries = getAllData();
            Log.d("entries length", String.valueOf(entries.size()));
            displayDataOnScatterChart(entries);
            int[] importantData = importantData(entries);
            Log.d("entries length", String.valueOf(importantData.length));
            return importantData;
        }

        @Override
        protected void onPostExecute(int[] importantData) {

            hourlyAQI = findViewById(R.id.hourlyIndoorAQI);
            dailyAQI = findViewById(R.id.dailyIndoorAQI);

            dailyAQI.setText(Integer.toString(importantData[1]));
            hourlyAQI.setText(Integer.toString(importantData[0]));

            if (importantData[0] < 50) {
                hourlyAQI.setTextColor(Color.GREEN);
            }
            else if (importantData[0] < 100) {
                hourlyAQI.setTextColor(Color.YELLOW);
            }
            else if (importantData[0] < 150) {
                hourlyAQI.setTextColor(Color.rgb(255,140,0));
            }
            else if (importantData[0] < 200) {
                hourlyAQI.setTextColor(Color.RED);
            }
            else if (importantData[0] < 300) {
                hourlyAQI.setTextColor(Color.rgb(128,0,128));
            }
            else {
                hourlyAQI.setTextColor(Color.rgb(165,42,42));

            }

            if (importantData[1] < 50) {
                dailyAQI.setTextColor(Color.GREEN);
            }
            else if (importantData[1] < 100) {
                dailyAQI.setTextColor(Color.YELLOW);
            }
            else if (importantData[1] < 150) {
                dailyAQI.setTextColor(Color.rgb(255,140,0));
            }
            else if (importantData[1] < 200) {
                dailyAQI.setTextColor(Color.RED);
            }
            else if (importantData[1] < 300) {
                dailyAQI.setTextColor(Color.rgb(128,0,128));
            }
            else {
                dailyAQI.setTextColor(Color.rgb(165,42,42));

            }

            int indoorCurrentAQI = importantData[2];
            String aqiString = "" + (int) indoorCurrentAQI;
            currentIndoorAQI.setText(aqiString);

            if (indoorCurrentAQI < 50) {
                currentIndoorDesc.setText("GOOD");
                currentIndoorAQI.setTextColor(Color.GREEN);
                currentIndoorDesc.setTextColor(Color.GREEN);

            }
            else if (indoorCurrentAQI < 100) {
                currentIndoorDesc.setText("MODERATE");
                currentIndoorAQI.setTextColor(Color.YELLOW);
                currentIndoorDesc.setTextColor(Color.YELLOW);

            }
            else if (indoorCurrentAQI < 150) {
                currentIndoorDesc.setText("UNHEALTHY FOR SENSITIVE GROUPS");
                currentIndoorAQI.setTextColor(Color.rgb(255,140,0));
                currentIndoorDesc.setTextColor(Color.rgb(255,140,0));
            }
            else if (indoorCurrentAQI < 200) {
                currentIndoorDesc.setText("UNHEALTHY");
                currentIndoorAQI.setTextColor(Color.RED);
                currentIndoorDesc.setTextColor(Color.RED);
            }
            else if (indoorCurrentAQI < 300) {
                currentIndoorDesc.setText("VERY UNHEALTHY");
                currentIndoorAQI.setTextColor(Color.rgb(128,0,128));
                currentIndoorDesc.setTextColor(Color.rgb(128,0,128));
            }
            else {
                currentIndoorDesc.setText("HAZARDOUS");
                currentIndoorAQI.setTextColor(Color.rgb(165,42,42));
                currentIndoorDesc.setTextColor(Color.rgb(165,42,42));

            }


            storeData(importantData);

        }


    }




    public List<Entry> getAllData() {

        List<Entry> entries = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String serialNumber = retrieveSerialNumber();
        Log.d("serial number", serialNumber);
        String baseUrl = "https://servicedeath.backendless.app/api/data/IndoorData?where=deviceSerialNumber="+serialNumber;
        int pageSize = 50;
        int offset = 0;



        while (true) {


            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://servicedeath.backendless.app/api/data/IndoorData?where=deviceSerialNumber=" + serialNumber + "&pageSize=" + pageSize + "&offSet=" + offset + "&sortBy=created%20desc")
                    .get()
                    .build();

            //try (Response response = client.newCall(request).execute()) {
            try (Response response = client.newCall(request).execute()) {

                if (response.code() == 200) {

                    String jsonData = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonData);

                    entries = addEntries(jsonArray, entries);

                    offset += pageSize;
                    if (jsonData.length() < pageSize) {
                        break;
                    }

                }

            }

            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        //displayDataOnLineChart(entries);

        return entries;
    }

    public List<Entry> addEntries(JSONArray jsonArray, List<Entry> entries) throws JSONException {


        for (int i = 0; i < jsonArray.length(); i++) {

            //single object
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //Log.d("object", String.valueOf(jsonObject));
            String aqi = jsonObject.getString("aqi");

            //time is in milliseconds
            String created = jsonObject.getString("created");

            //conversions
            long epoch = Long.parseLong(created);
            Date date = new Date(epoch);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(date);

            //extracting/truncating the hour as a decimal from the timestamp data
            String hourString = dateString.substring(dateString.indexOf(" ") + 1, dateString.indexOf(":"));
            String minuteString = dateString.substring(dateString.indexOf(":") + 1);
            minuteString = minuteString.substring(0, minuteString.indexOf(":"));

            float hourTotalFloat = parseInt(hourString) + (parseInt(minuteString) / 60.0f);

            int aqiInt = parseInt(aqi);

            //Log.d("ErrorLog", hourString);
            //Log.d("ErrorLog", aqi);

            entries.add(new Entry(epoch, aqiInt));

            //if (i == jsonArray.length()-1) {
            //    indoorCurrentAQI = aqiFloat;
           // }
        }

        return entries;

    }


    public void displayDataOnScatterChart(List<Entry> entries) {
        List<Entry> newEntries = new ArrayList<>(entries.size());
        Log.d("size", String.valueOf(entries.size()));
        //for some reason it needs to be size-1
        for (int i = 0; i < entries.size(); i++) {
            Date date = new Date((long) entries.get(i).getX());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(date);

            //extracting/truncating the hour as a decimal from the timestamp data
            String hourString = dateString.substring(dateString.indexOf(" ") + 1, dateString.indexOf(":"));
            String minuteString = dateString.substring(dateString.indexOf(":") + 1);
            minuteString = minuteString.substring(0, minuteString.indexOf(":"));

            float hourTotalFloat = parseInt(hourString) + (parseInt(minuteString) / 60.0f);
            float aqiFloat = entries.get(i).getY();
            //newEntries.add(new Entry(hourTotalFloat, aqiFloat));
            newEntries.add(new Entry(entries.get(i).getX(), aqiFloat));

        }
        Log.d("new entries to graph", String.valueOf(newEntries));
        Collections.sort(newEntries, new EntryXComparator());
        ScatterDataSet dataSet = new ScatterDataSet(newEntries, "Indoor Data");
        //ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        //dataSets.add(dataSet);

        dataSet.setDrawValues(false);
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(10f);


        ScatterData data = new ScatterData(dataSet);

        XAxis x = scatterChart.getXAxis();
        x.setDrawLabels(false);
        //x.setAxisMinimum(0f);
        //x.setAxisMaximum(24f);

        scatterChart.setData(data);

        //refresh
        scatterChart.invalidate();

    }

    public String retrieveSerialNumber() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String serialNumber = sp.getString("deviceSerialNumber", "");
        return serialNumber;
    }

    public boolean retrieveAsthmatic() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        boolean isAsthmatic = sp.getBoolean("asthmatic", false);
        return isAsthmatic;
    }

    public void storeData(int[] importantData) {

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        // Get the editor object
        SharedPreferences.Editor editor = sp.edit();

        // Add the String values to the editor object
        editor.putString("desc", (String) currentIndoorDesc.getText());
        editor.putInt("hourlyAvg", importantData[0]);
        editor.putInt("dailyAvg", importantData[1]);
        editor.putInt("curr", importantData[2]);


        // Save the changes
        editor.commit();
    }

    public int[] importantData(List<Entry> data) {
        int[] importantData = new int[3];
        float hourSum = 0, daySum = 0;
        int hourCount = 0, dayCount = 0;
        try {
            float timeToCompare = data.get(0).getX();
            Log.d("time to compare", Float.toString(timeToCompare));
            for (Entry entry : data) {
                //Log.d("entry", String.valueOf(entry));

                if (Math.abs(entry.getX() - timeToCompare) <= 3600000) {
                    hourSum += entry.getY();
                    hourCount++;


                }

                if (Math.abs(entry.getX() - timeToCompare) <= 86400000) {
                    daySum += entry.getY();
                    dayCount++;


                }
            }

            importantData[0] = (int) (hourSum / hourCount);
            importantData[1] = (int) (daySum / dayCount);
            importantData[2] = (int) data.get(0).getY();

        }
        catch (IndexOutOfBoundsException e) {

            return new int[3];

        }

        return importantData;
    }

}

