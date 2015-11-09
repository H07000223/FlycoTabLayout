package com.flyco.tablayout.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

public class FragmentChangeManager {
    private FragmentManager fm;
    private int containerViewId;
    /** Fragment切换数组 */
    private ArrayList<Fragment> fragments;
    /** 当前选中的Tab */
    private int currentTab;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.fm = fm;
        this.containerViewId = containerViewId;
        this.fragments = fragments;
        initFragments();
    }

    /** 初始化fragments */
    private void initFragments() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(containerViewId, fragment).hide(fragment).commit();
        }

        setFragments(0);
    }

    /** 界面切换控制 */
    public void setFragments(int index) {
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = fragments.get(i);
            if (i == index) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = index;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }
}