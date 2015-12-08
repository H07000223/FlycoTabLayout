package com.flyco.tablayoutsamples.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayoutsamples.R;
import com.flyco.tablayoutsamples.utils.ViewFindUtils;

import java.util.ArrayList;

public class SegmentTabActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<Fragment> fragments2 = new ArrayList<>();

    private String[] titles = {"首页", "消息"};
    private String[] titles_2 = {"首页", "消息", "联系人"};
    private String[] titles_3 = {"首页", "消息", "联系人", "更多"};
    private View decorView;
    private SegmentTabLayout tl_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_tab);

        for (String title : titles_3) {
            fragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
        }

        for (String title : titles_2) {
            fragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }

        decorView = getWindow().getDecorView();

        SegmentTabLayout tl_1 = ViewFindUtils.find(decorView, R.id.tl_1);
        SegmentTabLayout tl_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        tl_3 = ViewFindUtils.find(decorView, R.id.tl_3);
        SegmentTabLayout tl_4 = ViewFindUtils.find(decorView, R.id.tl_4);
        SegmentTabLayout tl_5 = ViewFindUtils.find(decorView, R.id.tl_5);

        tl_1.setTabData(titles);
        tl_2.setTabData(titles_2);
        tl_3();
        tl_4.setTabData(titles_2, this, R.id.fl_change, fragments2);
        tl_5.setTabData(titles_3);

        //显示未读红点
        tl_1.showDot(2);
        tl_2.showDot(2);
        tl_3.showDot(1);
        tl_4.showDot(1);

        //设置未读消息红点
        tl_3.showDot(2);
        RoundTextView rtv_3_2 = tl_3.getMsgView(2);
        if (rtv_3_2 != null) {
            rtv_3_2.getDelegate().setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void tl_3() {
        final ViewPager vp_3 = ViewFindUtils.find(decorView, R.id.vp_2);
        vp_3.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        tl_3.setTabData(titles_3);
        tl_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_3.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tl_3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_3.setCurrentItem(1);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles_3[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
