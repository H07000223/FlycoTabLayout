package com.flyco.tablayout.widget;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Created by Administrator .
 * 描述
 */
public class ScaleInTransformer implements ViewPager2.PageTransformer {

    public static final float DEFAULT_MIN_SCALE = 0.85F;
    public static final float DEFAULT_CENTER = 0.5F;
    private final float mMinScale = DEFAULT_MIN_SCALE;

    @Override
    public void transformPage(@NonNull View page, float position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            page.setElevation(-Math.abs(position));
        }

        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        page.setPivotY(pageHeight / 2f);
        page.setPivotX(pageWidth / 2f);

        if (position < -1) {
            page.setScaleX(this.mMinScale);
            page.setScaleY(this.mMinScale);
            page.setPivotX(pageWidth);
        } else if (position <= 1) {
            float scaleFactor;
            if (position < 0) {
                scaleFactor = (1 + position) * (1 - this.mMinScale) + this.mMinScale;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position));
            } else {
                scaleFactor = (1 - position) * (1 - this.mMinScale) + this.mMinScale;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * (1 - position) * DEFAULT_CENTER);
            }
        } else {
            page.setPivotX(0.0F);
            page.setScaleX(this.mMinScale);
            page.setScaleY(this.mMinScale);
        }
    }
}
