package com.flyco.tablayout.utils;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;

/**
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class UnreadMsgUtils {
    public static void show(RoundTextView rtv, int num) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        DisplayMetrics dm = rtv.getResources().getDisplayMetrics();
        RoundViewDelegate delegate = rtv.getDelegate();
        rtv.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            delegate.setStrokeWidth(0);
            rtv.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            rtv.setLayoutParams(lp);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num > 0 && num < 10) {//圆
                lp.width = (int) (18 * dm.density);
                rtv.setText(num + "");
            } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                rtv.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                rtv.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                rtv.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                rtv.setText("99+");
            }
            rtv.setLayoutParams(lp);
        }
    }

    public static void setSize(RoundTextView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
