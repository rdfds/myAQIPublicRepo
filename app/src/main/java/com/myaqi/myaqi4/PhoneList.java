package com.myaqi.myaqi4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.myaqi.myaqi4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneList extends AppCompatActivity {
    public static String phone1, phone2, phone3, phone4, phone5, email;
    private EditText editTextPhone1;
    private EditText editTextPhone2;
    private EditText editTextPhone3;
    private EditText editTextPhone4;
    private EditText editTextPhone5;

    private Button buttonSave;

    //private OkHttpClient client;
    //private static final String URL = "https://servicedeath.backendless.app/api/data/PhoneList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_list);

        editTextPhone1 = findViewById(R.id.editTextPhone1);
        editTextPhone2 = findViewById(R.id.editTextPhone2);
        editTextPhone3 = findViewById(R.id.editTextPhone3);
        editTextPhone4 = findViewById(R.id.editTextPhone4);
        editTextPhone5 = findViewById(R.id.editTextPhone5);

        buttonSave = findViewById(R.id.buttonSave);

        //client = new OkHttpClient();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoneList();
                startActivity(new Intent(getApplicationContext(), SetupDevice.class));
            }
        });
    }

    private void savePhoneList() {
        email = retrieveEmail();
        phone1 = editTextPhone1.getText().toString().trim();
        phone2 = editTextPhone2.getText().toString().trim();
        phone3 = editTextPhone3.getText().toString().trim();
        phone4 = editTextPhone4.getText().toString().trim();
        phone5 = editTextPhone5.getText().toString().trim();
        Log.d(phone1, "phone1");
        Log.d(phone2, "phone2");
        Log.d(phone3, "phone3");
        Log.d(phone4, "phone4");
        Log.d(phone5, "phone5");
        Log.d(email, "email");

        /*String requestBody = "email=" + email +
                "&phone1=" + phone1 +
                "&phone2=" + phone2 +
                "&phone3=" + phone3 +
                "&phone4=" + phone4 +
                "&phone5=" + phone5;*/

        SavePhoneListTask task = new SavePhoneListTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String retrieveEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            return user.getEmail();
        }

        return "";
    }

    private class SavePhoneListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url("https://myaqifinal.vercel.app/api/phonelist?email="+email+"&phone1="+phone1+"&phone2="+phone2+"&phone3="+phone3+"&phone4="+phone4+"&phone5="+phone5)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            try (Response response = client.newCall(request).execute()) {

            }

            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }
}