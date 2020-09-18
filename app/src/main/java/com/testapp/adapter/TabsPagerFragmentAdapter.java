package com.testapp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.testapp.fragment.TeacherTestFragment;
import com.testapp.fragment.TeacherResultFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Мои тесты",
                "Результаты"
        };
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TeacherTestFragment.getInstance();
            case 1:
                return TeacherResultFragment.getInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
