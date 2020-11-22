package com.progmobklp1.aplikasipresensi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.adapter.HomeFragmentAdapter;
import com.progmobklp1.aplikasipresensi.fragment.HomeFragment;
import com.progmobklp1.aplikasipresensi.fragment.PresensiFragment;

public class HomeActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_assignment_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TabLayout homeTabLayout = findViewById(R.id.home_tab_bar);
        TabItem homeTab = findViewById(R.id.home_tab);
        TabItem presensiTab = findViewById(R.id.presensi_tab);
        ViewPager homeViewPager = findViewById(R.id.home_view_pager);

        homeTabLayout.setupWithViewPager(homeViewPager);

        HomeFragmentAdapter homeViewPagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFrag(new HomeFragment(), "Home");
        homeViewPagerAdapter.addFrag(new PresensiFragment(), "Presensi");
        homeViewPager.setAdapter(homeViewPagerAdapter);

        homeTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        homeTabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}