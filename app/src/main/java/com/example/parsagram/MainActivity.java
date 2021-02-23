package com.example.parsagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    private Button take_photo, submit;
    private EditText caption;
    private ImageView photo_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        take_photo = findViewById(R.id.Take_picture);
        submit = findViewById(R.id.Submit_button);
        caption = findViewById(R.id.Caption_textbox);
        photo_preview = findViewById(R.id.photo_box);


    }
}