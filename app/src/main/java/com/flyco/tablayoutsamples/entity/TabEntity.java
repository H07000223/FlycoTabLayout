package com.flyco.tablayoutsamples.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public String selectedIconUrl;
    public String unSelectedIconUrl;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title, int selectedIcon, int unSelectedIcon, String selectedIconUrl, String unSelectedIconUrl) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
        this.selectedIconUrl = selectedIconUrl;
        this.unSelectedIconUrl = unSelectedIconUrl;
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
    public String getSelectedIconUrl() {
        return selectedIconUrl;
    }

    @Override
    public String getUnSelectedIconUrl() {
        return unSelectedIconUrl;
    }

    public void setSelectedIconUrl(String selectedIconUrl) {
        this.selectedIconUrl = selectedIconUrl;
    }

    public void setUnSelectedIconUrl(String unSelectedIconUrl) {
        this.unSelectedIconUrl = unSelectedIconUrl;
    }
}
