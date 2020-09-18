package com.testapp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.testapp.fragment.StudentTestFragment;
import com.testapp.fragment.StudentResultFragment;

public class TabsFragmentAdapterStudents extends FragmentPagerAdapter {
    private String[] tabs;

    public TabsFragmentAdapterStudents(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Список тестов",
                "Мои результаты"
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
                return StudentTestFragment.getInstance();

            case 1:
                return StudentResultFragment.getInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
