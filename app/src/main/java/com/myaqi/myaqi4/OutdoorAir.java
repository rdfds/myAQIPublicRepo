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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.utils.EntryXComparator;
import com.myaqi.myaqi4.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
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

public class OutdoorAir extends AppCompatActivity {


    TextView currentOutdoorAQI, currentOutdoorDesc;
    LinearLayout currOutdoorAQIBackground;
    ScatterChart scatterChart;
    FirebaseAuth fAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_air);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView OutdoorTitle = findViewById(R.id.OutdoorAQI);
        OutdoorTitle.setText("OUTDOOR AIR QUALITY");
        currentOutdoorDesc = findViewById(R.id.currentOutdoorDesc);
        currentOutdoorAQI = findViewById(R.id.currentOutdoorAQI);
        currOutdoorAQIBackground = findViewById(R.id.currOutdoorAQIBackground);
        //logoutbtn = (Button) findViewById(R.id.logout);
        fAuth = FirebaseAuth.getInstance();
        scatterChart = findViewById(R.id.outdoorScatterplot);
        OutdoorAir.OutdoorDataApiCall OutdoorDataApiCall = new OutdoorAir.OutdoorDataApiCall();
        OutdoorDataApiCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //int[] averages = average(data);
        //updateDisplay();

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
                    R.id.indoor_air:

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private class OutdoorDataApiCall extends AsyncTask<Void, Void, int[]> {

        private TextView hourlyAQI, dailyAQI;

        @Override
        protected int[] doInBackground(Void... voids) {

            List<Entry> entries = getAllData();
            int[] importantData = importantData(entries);
            return importantData;
        }

        @Override
        protected void onPostExecute(int[] importantData) {
            hourlyAQI = findViewById(R.id.hourlyOutdoorAQI);
            dailyAQI = findViewById(R.id.dailyOutdoorAQI);

            dailyAQI.setText(Integer.toString(importantData[1]));
            hourlyAQI.setText(Integer.toString(importantData[0]));


            /*if (importantData[0] < 50) {
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

            }*/

            int outdoorCurrentAQI = importantData[2];
            String aqiString = "" + (int) outdoorCurrentAQI;
            currentOutdoorAQI.setText(aqiString);

            if (outdoorCurrentAQI < 50) {
                currentOutdoorDesc.setText("GOOD");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_good);
                //currentOutdoorAQI.setTextColor(Color.GREEN);
                //currentOutdoorDesc.setTextColor(Color.GREEN);

            }
            else if (outdoorCurrentAQI < 100) {
                currentOutdoorDesc.setText("MODERATE");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_moderate);
                //currentOutdoorAQI.setTextColor(Color.YELLOW);
                //currentOutdoorDesc.setTextColor(Color.YELLOW);

            }
            else if (outdoorCurrentAQI < 150) {
                currentOutdoorDesc.setText("UNHEALTHY FOR SENSITIVE GROUPS");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_unhealthy_for_sensitive_groups);
                //currentOutdoorAQI.setTextColor(Color.rgb(255,140,0));
               // currentOutdoorDesc.setTextColor(Color.rgb(255,140,0));
            }
            else if (outdoorCurrentAQI < 200) {
                currentOutdoorDesc.setText("UNHEALTHY");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_unhealthy);
                //currentOutdoorAQI.setTextColor(Color.RED);
                //currentOutdoorDesc.setTextColor(Color.RED);
            }
            else if (outdoorCurrentAQI < 300) {
                currentOutdoorDesc.setText("VERY UNHEALTHY");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_very_unhealthy);
                //currentOutdoorAQI.setTextColor(Color.rgb(128,0,128));
                //currentOutdoorDesc.setTextColor(Color.rgb(128,0,128));
            }
            else {
                currentOutdoorDesc.setText("HAZARDOUS");
                currOutdoorAQIBackground.setBackgroundResource(R.color.aqi_hazardous);
                //currentOutdoorAQI.setTextColor(Color.rgb(165,42,42));
                //currentOutdoorDesc.setTextColor(Color.rgb(165,42,42));

            }

        }


    }


    public List<Entry> getAllData() {

        List<Entry> entries = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String serialNumber = retrieveSerialNumber();
        Log.d("serial number", serialNumber);
        int pageSize = 50;
        int offset = 0;



        while (true) {


            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://servicedeath.backendless.app/api/data/OutdoorData?where=deviceSerialNumber=" + serialNumber + "&pageSize=" + pageSize + "&offSet=" + offset + "&sortBy=created%20desc")
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
        displayDataOnScatterChart(entries);

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
            if (!aqi.equals("null")) {
                int aqiInt = parseInt(aqi);
                entries.add(new Entry(epoch, aqiInt));
            }

            //Log.d("ErrorLog", hourString);
            //Log.d("ErrorLog", aqi);



            /*if (i == jsonArray.length()-1) {
                indoorCurrentAQI = aqiFloat;
            }*/
        }

        return entries;

    }


    public void displayDataOnScatterChart(List<Entry> entries) {
        List<Entry> newEntries = new ArrayList<>();

        for (int i = 0; i < entries.size()-1; i++) {
            Date date = new Date((long) entries.get(i).getX());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(date);

            //extracting/truncating the hour as a decimal from the timestamp data
            String hourString = dateString.substring(dateString.indexOf(" ") + 1, dateString.indexOf(":"));
            String minuteString = dateString.substring(dateString.indexOf(":") + 1);
            minuteString = minuteString.substring(0, minuteString.indexOf(":"));

            float hourTotalFloat = parseInt(hourString) + (parseInt(minuteString) / 60.0f);

            //newEntries.add(new Entry(hourTotalFloat, (float) entries.get(i).getY()));
            newEntries.add(new Entry(entries.get(i).getX(), entries.get(i).getY()));
        }
        Collections.sort(newEntries, new EntryXComparator());

        ScatterDataSet dataSet = new ScatterDataSet(newEntries, "Outdoor Data");
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(10f);



        ScatterData data = new ScatterData(dataSet);
        scatterChart.setData(data);

        XAxis x = scatterChart.getXAxis();
        x.setDrawLabels(false);
        //x.setAxisMinimum(0f);
       // x.setAxisMaximum(24f);

        //refresh
        scatterChart.invalidate();

    }

    public String retrieveSerialNumber() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String serialNumber = sp.getString("deviceSerialNumber", "");
        return serialNumber;
    }

    public int[] importantData(List<Entry> data) {
        int[] importantData = new int[3];
        float hourSum = 0, daySum = 0;
        int hourCount = 0, dayCount = 0;
        try {
            float timeToCompare = data.get(0).getX();
            Log.d("time to compare", Float.toString(timeToCompare));
            for (Entry entry : data) {
                Log.d("entry", String.valueOf(entry));

                if (Math.abs(entry.getX() - timeToCompare) <= 3600000) {
                    hourSum += entry.getY();
                    hourCount++;


                }

                if (Math.abs(entry.getX() - timeToCompare) <= 86400000) {
                    daySum += entry.getY();
                    dayCount++;


                }
            }
            Log.d("hour sum", Float.toString(hourSum));
            Log.d("hour count", Float.toString(hourCount));
            Log.d("day sum", Float.toString(daySum));
            Log.d("day count", Float.toString(dayCount));
            importantData[0] = (int) (hourSum / hourCount);
            importantData[1] = (int) (daySum / dayCount);
            Log.d("current AQI", String.valueOf(data.get(0).getY()));
            importantData[2] = (int) data.get(0).getY();
        }
        catch (IndexOutOfBoundsException e) {
            return new int[3];
        }
        return importantData;
    }

}

