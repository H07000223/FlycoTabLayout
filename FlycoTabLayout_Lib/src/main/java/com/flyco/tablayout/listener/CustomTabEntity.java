package com.flyco.tablayout.listener;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();

    Drawable getTabSelectedDrawable();
    Drawable getTabUnselectedDrawable();
}