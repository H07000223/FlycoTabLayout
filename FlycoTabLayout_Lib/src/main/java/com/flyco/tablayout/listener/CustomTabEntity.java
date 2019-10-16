package com.flyco.tablayout.listener;

import androidx.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();

    /*以下网络图片*/

    String getSelectedIconUrl();

    String getUnSelectedIconUrl();

    void setSelectedIconUrl(String selectedIconUrl);

    void setUnSelectedIconUrl(String unSelectedIconUrl);
}