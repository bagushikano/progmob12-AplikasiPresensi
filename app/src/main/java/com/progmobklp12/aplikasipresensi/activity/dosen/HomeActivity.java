package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.AboutActivity;
import com.progmobklp12.aplikasipresensi.activity.LoginActivity;
import com.progmobklp12.aplikasipresensi.adapter.HomeFragmentAdapter;
import com.progmobklp12.aplikasipresensi.fragment.dosen.DosenListFragment;
import com.progmobklp12.aplikasipresensi.fragment.dosen.HomeFragment;
import com.progmobklp12.aplikasipresensi.fragment.dosen.MahasiswaListFragment;
import com.progmobklp12.aplikasipresensi.fragment.dosen.PresensiFragment;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences loginPreferences;
    private int loginStatus;
    private TextView welcomeText;
    private String namaUser;

    Fragment fragmentDosenList;
    Fragment fragmentMahasiswaList;

    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_assignment_24,
            R.drawable.ic_baseline_groups_24,
            R.drawable.ic_baseline_person_24,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


        TabLayout homeTabLayout = findViewById(R.id.home_tab_bar);
        TabItem homeTab = findViewById(R.id.home_tab);
        TabItem presensiTab = findViewById(R.id.presensi_tab);
        ViewPager homeViewPager = findViewById(R.id.home_view_pager);

        homeTabLayout.setupWithViewPager(homeViewPager);

        HomeFragmentAdapter homeViewPagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFrag(new HomeFragment(), "Home");
        homeViewPagerAdapter.addFrag(new PresensiFragment(), "Presensi");
        homeViewPagerAdapter.addFrag(new MahasiswaListFragment(), "Mahasiswa");
        homeViewPagerAdapter.addFrag(new DosenListFragment(), "Dosen");
        homeViewPager.setAdapter(homeViewPagerAdapter);

        homeTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        homeTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        homeTabLayout.getTabAt(2).setIcon(tabIcons[2]);
        homeTabLayout.getTabAt(3).setIcon(tabIcons[3]);

        fragmentDosenList = homeViewPagerAdapter.getItem(3);

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
        if (id == R.id.action_logout) {
            loginStatus = loginPreferences.getInt("login_status", 0);
            if (loginStatus != 0) {
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putInt("login_status", 0);
                editor.apply();
            }
            Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentDosenList.onActivityResult(requestCode, resultCode, data);
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }
}