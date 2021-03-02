package com.example.parsagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import com.example.parsagram.fragments.Compose_Fragment;
import com.example.parsagram.fragments.Post_Fragment;
import com.example.parsagram.fragments.Profile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_view;
    private Toolbar top_menu;
    private final FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation_view = findViewById(R.id.bottom_navigation);
        top_menu = findViewById(R.id.tool_bar);

        setSupportActionBar(top_menu);

        top_menu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.sign_out) {
                    sign_out();
                    return true;
                }

                return false;
            }
        });

        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new Post_Fragment();
                        break;
                    case R.id.action_compose:
                        fragment = new Compose_Fragment();
                        break;
                    case R.id.action_profile:
                        fragment = new Profile_Fragment();
                        break;
                    default:
                        return false;
                }

                manager.beginTransaction().replace(R.id.frame_layout_container, fragment).commit();
                return true;
            }
        });

        bottom_navigation_view.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        return true;
    }

    public void sign_out() {
        ParseUser.logOutInBackground();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}