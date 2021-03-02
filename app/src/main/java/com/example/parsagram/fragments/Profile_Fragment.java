package com.example.parsagram.fragments;

import android.util.Log;
import com.example.parsagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Profile_Fragment extends Post_Fragment {

    private final String TAG = getClass().getCanonicalName();

    @Override
    protected void query_posts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED);

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

                my_posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

}


