package com.flyco.tablayout.listener;

import android.graphics.drawable.Drawable;

/**
 * Created by lizk on 2017/11/1.
 */

public class TabEntity implements CustomTabEntity {

    private String tabTitle;
    private Drawable selectedDrawable;
    private Drawable unselectedDrawable;
    private int selectedIcon;
    private int unSelectedIcon;

    public TabEntity(String tabTitle, Drawable selectedDrawable, Drawable unselectedDrawable) {
        this.tabTitle = tabTitle;
        this.selectedDrawable = selectedDrawable;
        this.unselectedDrawable = unselectedDrawable;
    }

    public TabEntity(String tabTitle, int selectedIcon, int unselectedIcon) {
        this.tabTitle = tabTitle;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unselectedIcon;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    @Override
    public Drawable getTabSelectedDrawable() {
        return selectedDrawable;
    }

    @Override
    public Drawable getTabUnselectedDrawable() {
        return unselectedDrawable;
    }
}
