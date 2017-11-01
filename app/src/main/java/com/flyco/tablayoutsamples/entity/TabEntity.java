package com.flyco.tablayoutsamples.entity;

import android.graphics.drawable.Drawable;

import com.flyco.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public Drawable selectedDrawable;
    public Drawable unselectedDrawable;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
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
