package com.myaqi.myaqi4;

import static com.myaqi.myaqi4.Register.userID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myaqi.myaqi4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetupDevice extends AppCompatActivity implements LocationListener {

    public static double latitude, longitude;
    public static String deviceSerialNumber;
    Button locationButton, submitButton, backButton;
    EditText mDeviceSerialNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_device);
        locationButton = findViewById(R.id.locationButton);
        submitButton = findViewById(R.id.submitButton);
        mDeviceSerialNumber = findViewById(R.id.deviceSerialNumber);
        backButton = findViewById(R.id.backButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deviceSerialNumber= mDeviceSerialNumber.getText().toString().trim();
                SetupDevice.getUserID getUserID = new SetupDevice.getUserID();
                //getUserID.execute();
                //getUserID.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                storeSerialNumber();
                SetupDevice.RegisterDevice RegisterDevice = new SetupDevice.RegisterDevice();
                RegisterDevice.execute();
                //RegisterDevice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //SetupDevice.CheckValid CheckValid = new SetupDevice.CheckValid();
                //CheckValid.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }

    public void getLocation(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private void retrieveLocation() {

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //SetupDevice.RegisterDevice RegisterDevice = new SetupDevice.RegisterDevice();
            //RegisterDevice.execute();



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    private class RegisterDevice extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("latitude", Double.toString(latitude));
            Log.d("longitude", Double.toString(longitude));


            Request.Builder builder = new Request.Builder();

            Request request = builder
                    //.url("https://myaqifinal.vercel.app/api/registerdevice?deviceserialnumber="+deviceSerialNumber+"&device_owner="+userID+"&latitude="+latitude+"&longitude="+longitude)
                    .url("https://myaqifinal.vercel.app/api/registerdevice?deviceserialnumber="+deviceSerialNumber+"&device_owner="+retrieveEmail()+"&latitude="+latitude+"&longitude="+longitude)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            try (Response response = client.newCall(request).execute()) {
                Log.d("response", String.valueOf(response));
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class getUserID extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Request.Builder builder = new Request.Builder();
            String email = retrieveEmail();

            Request request = builder
                    .url("https://servicedeath.backendless.app/api/data/User?where=email%20=%20%27" +email+ "%27")
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            try (Response response = client.newCall(request).execute()) {

                if (response.code() == 200) {

                    String jsonData = response.body().string();
                    Log.d("json stuff", jsonData);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    userID = jsonObject.getString("objectId");
                    Log.d(userID, "userId");


                }

            }

            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public String retrieveEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            return user.getEmail();
        }

        return "";


    }

   /* private class CheckValid extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://servicedeath.backendless.app/api/data/IndoorData?where=deviceSerialNumber=" + deviceSerialNumber)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            Log.d("hi", "hi");
            try (Response response = client.newCall(request).execute()) {

                if (response.code() == 200) {

                    String jsonData = response.body().string();
                    //JSONArray jsonArray = new JSONArray(jsonData);
                    //Log.d("length", String.valueOf(jsonArray.length()));
                    if (!jsonData.equals("[]")) {
                        Toast.makeText(getApplicationContext(), "The serial number is invalid", Toast.LENGTH_SHORT).show();
                        storeSerialNumber();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), SetupDevice.class));
                    }

                }

            }

            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }*/

    public void storeSerialNumber() {
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        // Get the editor object
        SharedPreferences.Editor editor = sp.edit();

        // Add the String value to the editor object
        editor.putString("deviceSerialNumber", deviceSerialNumber);

        // Save the changes
        editor.commit();
    }
}


/*public void accessLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5F, (LocationListener) this);

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            SetupDevice.RegisterDevice RegisterDevice = new SetupDevice.RegisterDevice();
            RegisterDevice.execute();



        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        }
    }

    /*@Override
    public void onLocationChanged(@NonNull Location location) {

    }*/


