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
    public static final String KEY_DESCRIPTION = "description", KEY_IMAGE = "image", KEY_USER = "user", KEY_CREATED = "createdAt";

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

}
