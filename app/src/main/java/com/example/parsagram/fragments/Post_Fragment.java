package com.example.parsagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parsagram.Post;
import com.example.parsagram.Posts_Adapter;
import com.example.parsagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Post_Fragment extends Fragment {

    private final String TAG = getClass().getCanonicalName();
    private RecyclerView post_feed;
    private SwipeRefreshLayout refresher;
    protected Posts_Adapter adapter;
    protected List<Post> my_posts;

    public Post_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        post_feed = view.findViewById(R.id.recycler_view);
        refresher = view.findViewById(R.id.swipe_to_refresh);
        my_posts = new ArrayList<>();

        refresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // steps for recycler view:

        // create the adapter
        adapter = new Posts_Adapter(getContext(), my_posts);
        // set the adapter on the recycler view
        post_feed.setAdapter(adapter);
        // set the layout manager on the recycler view
        post_feed.setLayoutManager(new LinearLayoutManager(getContext()));

        query_posts();

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query_posts();
            }
        });
    }

    protected void query_posts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.setLimit(20);
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