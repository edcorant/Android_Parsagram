package com.example.parsagram;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.time.temporal.TemporalAccessor;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    private final String TAG = getClass().getCanonicalName();
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String desc) {
        put(KEY_DESCRIPTION, desc);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser new_user) {
        put(KEY_USER, new_user);
    }

    private void query_posts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(KEY_USER);

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
