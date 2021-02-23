package com.example.parsagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    private EditText username_field, password_field;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null)
            go_to_main_activity();

        username_field = findViewById(R.id.Username_textbox);
        password_field = findViewById(R.id.Password_textbox);
        login_button = findViewById(R.id.Login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User pressed \'Login\'");
                String username = username_field.getText().toString();
                String password = password_field.getText().toString();
                login_user(username, password);
            }
        });
    }

    private void login_user(String username, String password) {
        Log.i(TAG, "Attempting to log in user " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue logging in.", e);
                    Toast.makeText(LoginActivity.this, "Issue logging in.", Toast.LENGTH_SHORT);
                    return;
                }

                go_to_main_activity();
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT);
            }
        });
    }

    private void go_to_main_activity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}