package com.progmobklp1.aplikasipresensi.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.fragment.HomeFragment;
import com.progmobklp1.aplikasipresensi.fragment.PresensiFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();

    public HomeFragmentAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    public void addFrag(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
