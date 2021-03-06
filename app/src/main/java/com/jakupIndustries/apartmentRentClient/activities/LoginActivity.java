package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.models.LoginDTO;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

import java.io.IOException;

import static com.jakupIndustries.apartmentRentClient.common.CommonMethod.isValidEmail;
import static com.jakupIndustries.apartmentRentClient.common.CommonMethod.isValidPassword;


public class LoginActivity extends AppCompatActivity {
    ApartmentRentService apartmentRentService;
    EditText loginEditText, passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //TODO FIX layout when keyboard visible
        setContentView(R.layout.activity_login);
        loginEditText = (EditText) findViewById(R.id.editTextUsernameOrEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextTextPassword);
        String email="";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                email = extras.getString("email");

            }
        } else {
            email = (String) savedInstanceState.getSerializable("email");
        }
        loginEditText.setText(email);

    }


    public void onLoginButtonClick(View view) throws InterruptedException, IOException {
        if (isEverythingOk()) {
            Call<Void> call = apartmentRentService.login(new LoginDTO(loginEditText.getText().toString(), passwordEditText.getText().toString()));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 200) {
                        String jsessionid = response.headers().get("Set-Cookie");
                        Log.d("LoginDTO callback: ", "Success || " + jsessionid);
                        Cookie.cookie = jsessionid;
                        //session.setjSessionID(jsessionid);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 401) {
                        Log.d("LoginDTO callback: ", "Invalid login data");
                        Toast.makeText(getApplicationContext(), "Login or password incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("LoginDTO callback: ", "unknown error || CODE: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    //TODO NON CONNECTION SUPPORT
                    Log.d("LoginDTO callback: ", t.getMessage());
                }
            });
        }
    }

    private boolean isEverythingOk() {
        boolean a = isValidEmail(loginEditText);
        boolean b = isValidPassword(passwordEditText);
        return (a && b);
    }


    @Override
    public void onBackPressed() {

    }

    public void onRegisterButtonClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}