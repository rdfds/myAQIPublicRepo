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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Register extends AppCompatActivity{

    //public static String firstName, lastName, phone, email, password, userID, username;
    public static String email, password, userID, username;
    EditText mFirstName, mLastName, mEmail, mPassword, mPhone, mUsername;
    Button mRegisterBtn;
    TextView sendToLogin;
    CheckBox mAsthmatic;


    FirebaseAuth fAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mFirstName = findViewById(R.id.firstName);
        //mLastName = findViewById(R.id.lastName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        //mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.register);
        sendToLogin = findViewById(R.id.sendToLogin);
        mUsername = findViewById(R.id.username);
        //mAsthmatic = findViewById(R.id.asthmatic);
        fAuth = FirebaseAuth.getInstance();

        //if the user is already logged in, use the in built firebase feature to place them in the main activity
        File directory = getFilesDir();
        File file = new File(directory, "user.txt");
        try {
            boolean success = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //on the click of the registration button
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //get the text stuff
                //firstName = mFirstName.getText().toString().trim();
                //lastName = mLastName.getText().toString().trim();
                //phone = mPhone.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                username = mUsername.getText().toString().trim();
                //error checking
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please enter an email.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please enter a password.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Your password must be 6 characters or more.");
                    return;
                }

                //register the user
                Task<SignInMethodQueryResult> signInMethods = fAuth.fetchSignInMethodsForEmail(email);
                signInMethods.addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.isEmpty()) {
                                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                                            RegistrationApiCall RegistrationApiCall = new RegistrationApiCall();
                                            RegistrationApiCall.execute();
                                            //RegistrationApiCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                            //startActivity(new Intent(getApplicationContext(), SetupDevice.class));
                                            startActivity(new Intent(getApplicationContext(), PhoneList.class));
                                        }
                                        else {
                                            Toast.makeText(Register.this, "?An Error Has Occurred. Please Try Again" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Register.this, "This email is already associated with an account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "An Error Has Occurred. Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }



        });

        /*mAsthmatic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle checkbox state changes here
                if (isChecked) {
                    storeAsthmatic(true);
                } else {
                    storeAsthmatic(false);
                }
            }
        });*/

        sendToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });




    }


    private class RegistrationApiCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    //.url("https://myaqifinal.vercel.app/api/registration?email="+email+"&firstname="+firstName+"&lastname="+lastName+"&password="+password+"&phone="+phone)
                    .url("https://myaqifinal.vercel.app/api/registration?email="+email+"&password="+password+"&username="+username)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            try (Response response = client.newCall(request).execute()) {

                if (response.code() == 200) {

                    String jsonData = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    userID = jsonObject.getString("userID");

                }

            }

            catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    public void storeAsthmatic(boolean isAsthmatic) {
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        // Get the editor object
        SharedPreferences.Editor editor = sp.edit();

        // Add the String value to the editor object
        editor.putBoolean("asthmatic", isAsthmatic);

        // Save the changes
        editor.commit();
    }

}


