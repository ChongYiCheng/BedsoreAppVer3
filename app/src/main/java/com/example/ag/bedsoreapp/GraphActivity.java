package com.example.ag.bedsoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class GraphActivity extends AppCompatActivity {
    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        // Create the toolbar for the tablayout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data");

        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        // Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.doctor:
                        Intent intent2 = new Intent(GraphActivity.this, DoctorChat.class);
                        startActivity(intent2);
                        break;
                    case R.id.medicalRecords:
                        Intent intent3 = new Intent(GraphActivity.this, Records.class);
                        startActivity(intent3);
                        break;

                }
                return true;
            }
        });


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SensorFragment1(), "Sensor 1");
        adapter.addFragment(new SensorFragment2(), "Sensor 2");
        adapter.addFragment(new SensorFragment3(), "Sensor 3");
        adapter.addFragment(new SensorFragment4(), "Sensor 4");
        viewPager.setAdapter(adapter);

    }
}
