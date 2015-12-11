package com.flyco.tablayoutsamples.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleHomeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mItems;
    private DisplayMetrics mDisplayMetrics;

    public SimpleHomeAdapter(Context context, String[] items) {
        this.mContext = context;
        this.mItems = items;
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int padding = (int) (mDisplayMetrics.density * 10);

        TextView tv = new TextView(mContext);
        tv.setText(mItems[position]);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(Color.parseColor("#468ED0"));
        // tv.setGravity(Gravity.CENTER);
        tv.setPadding(padding, padding, padding, padding);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        return tv;
    }
}