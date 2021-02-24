package com.example.parsagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName(), photo_file_name = "photo.jpg";
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private Button take_photo, submit, sign_out;
    private EditText caption;
    private ImageView photo_preview;
    private File photo_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        take_photo = findViewById(R.id.Take_picture);
        submit = findViewById(R.id.Submit_button);
        caption = findViewById(R.id.Caption_textbox);
        photo_preview = findViewById(R.id.photo_box);
        sign_out = findViewById(R.id.Sign_out_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = caption.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Cannot make post with empty description.", Toast.LENGTH_SHORT).show();
                }

                if (photo_file == null || photo_preview.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "Cannot make post without photo.", Toast.LENGTH_SHORT).show();
                }

                else {
                    ParseUser current = ParseUser.getCurrentUser();
                    savePost(description, current, photo_file);
                }
            }
        });

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_camera();
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                return_to_login_activity();
            }
        });
    }

    private void return_to_login_activity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void launch_camera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photo_file = getPhotoFileUri(photo_file_name);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photo_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photo_file.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                photo_preview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private File getPhotoFileUri(String photo_file_name) {

        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photo_file_name);
    }

    private void savePost(String description, ParseUser current, File photo_file) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photo_file));
        post.setUser(current);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Error while saving post:", e);
                    Toast.makeText(MainActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
                }

                else {
                    Log.i(TAG, "Post saved successfully.");
                    // clear description and photo preview to signal successful post
                    caption.setText("");
                    photo_preview.setImageResource(0);
                }
            }
        });
    }

    private void query_posts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Something went wrong while fetching posts");
                    return;
                }

                for (Post p : objects) {
                    Log.i(TAG, "Post: " + p.getDescription() + ", from user " + p.getUser().getUsername());
                }
            }
        });
    }
}