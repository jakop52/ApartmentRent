package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.models.RegisterDTO;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

import static com.jakupIndustries.apartmentRentClient.common.CommonMethod.isNotEmptyOnly;
import static com.jakupIndustries.apartmentRentClient.common.CommonMethod.isValidEmail;
import static com.jakupIndustries.apartmentRentClient.common.CommonMethod.isValidPassword;


public class RegisterActivity extends AppCompatActivity {
    ApartmentRentService apartmentRentService;
    EditText editTextName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //TODO FIX layout when keyboard visible
    }

    public void onRegisterButtonCLick(View view) {

        if (ifAllFieldsCorrect()) {
            Call<Void> call = apartmentRentService.register(new RegisterDTO(
                    editTextName.getText().toString(),
                    editTextEmail.getText().toString(),
                    editTextPassword.getText().toString()
            ));

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        String jsessionid = response.headers().get("Set-Cookie");
                        Log.d("LoginDTO callback: ", "Success || " + jsessionid);
                        Cookie.cookie = jsessionid;
                        //session.setjSessionID(jsessionid);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", editTextEmail.getText()); //TODO PREDICT EMAIL AFTER REGISTRATION
                        startActivity(intent);
                    } else if (response.code() == 401) {
                        Log.d("LoginDTO callback: ", "Invalid login data");
                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("LoginDTO callback: ", "unknown error || CODE: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        }
    }

    private boolean ifAllFieldsCorrect() {
        boolean a = isNotEmptyOnly(editTextName);
        boolean b = isValidEmail(editTextEmail);
        boolean c = isValidPassword(editTextPassword);
        return (a && b && c);
    }
}