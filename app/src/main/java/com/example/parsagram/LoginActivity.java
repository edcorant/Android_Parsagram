package com.example.parsagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    private EditText username_field, password_field;
    private Button login_button, signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null)
            go_to_main_activity();

        username_field = findViewById(R.id.Username_textbox);
        password_field = findViewById(R.id.Password_textbox);
        login_button = findViewById(R.id.Login_button);
        signup_button = findViewById(R.id.Sign_up_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User pressed \'Login\'");
                String username = username_field.getText().toString();
                String password = password_field.getText().toString();
                login_user(username, password);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_sign_up();
            }
        });
    }

    private void attempt_sign_up() {

        String username, password;
        username = username_field.getText().toString();
        password = password_field.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Sign Up requires Username and Password.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sign Up failed: empty username or password.");
            return;
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                // if error received
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "Sign Up failed.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Sign up failed:", e);
                }

                // if query result not empty
                else if (!objects.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username not available. Please pick a different one.", Toast.LENGTH_SHORT).show();
                    username_field.setText("");
                }

                // else, success
                else {
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            // if no errors
                            if (e == null)
                                login_user(username, password);

                            // else, notify user and write to log
                            else {
                                Toast.makeText(LoginActivity.this, "Sign Up failed.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Unsuccessful Sign Up:", e);
                                username_field.setText("");
                                password_field.setText("");
                            }
                        }
                    });
                }

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
                    Toast.makeText(LoginActivity.this, "Issue logging in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                go_to_main_activity();
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void go_to_main_activity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}