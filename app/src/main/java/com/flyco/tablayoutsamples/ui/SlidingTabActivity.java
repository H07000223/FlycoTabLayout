package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayoutsamples.R;
import com.flyco.tablayoutsamples.utils.ViewFindUtils;

import java.util.ArrayList;

public class SlidingTabActivity extends AppCompatActivity implements OnTabSelectListener {
    private Context context = this;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private final String[] titles = {
            "热门", "iOS", "Android", "前端"
            , "后端", "设计", "工具资源"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab);

        for (String title : titles) {
            fragments.add(SimpleCardFragment.getInstance(title));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** 默认 */
        SlidingTabLayout tl_1 = ViewFindUtils.find(decorView, R.id.tl_1);
        /**自定义部分属性*/
        SlidingTabLayout tl_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        /** 字体加粗,大写 */
        SlidingTabLayout tl_3 = ViewFindUtils.find(decorView, R.id.tl_3);
        /** tab固定宽度 */
        SlidingTabLayout tl_4 = ViewFindUtils.find(decorView, R.id.tl_4);
        /** indicator固定宽度 */
        SlidingTabLayout tl_5 = ViewFindUtils.find(decorView, R.id.tl_5);
        /** indicator圆 */
        SlidingTabLayout tl_6 = ViewFindUtils.find(decorView, R.id.tl_6);
        /** indicator矩形圆角 */
        SlidingTabLayout tl_7 = ViewFindUtils.find(decorView, R.id.tl_7);
        /** indicator三角形 */
        SlidingTabLayout tl_8 = ViewFindUtils.find(decorView, R.id.tl_8);
        /** indicator圆角色块 */
        SlidingTabLayout tl_9 = ViewFindUtils.find(decorView, R.id.tl_9);
        /** indicator圆角色块 */
        SlidingTabLayout tl_10 = ViewFindUtils.find(decorView, R.id.tl_10);

        tl_1.setViewPager(vp);
        tl_2.setViewPager(vp);
        tl_2.setOnTabSelectListener(this);
        tl_3.setViewPager(vp);
        tl_4.setViewPager(vp);
        tl_5.setViewPager(vp);
        tl_6.setViewPager(vp);
        tl_7.setViewPager(vp, titles);
        tl_8.setViewPager(vp, titles, this, fragments);
        tl_9.setViewPager(vp);
        tl_10.setViewPager(vp);

        vp.setCurrentItem(4);

        tl_1.showDot(4);
        tl_3.showDot(4);
        tl_2.showDot(4);

        tl_2.showMsg(3, 5);
        tl_2.setMsgMargin(3, 0, 10);
        RoundTextView rtv_2_3 = tl_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.getDelegate().setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tl_2.showMsg(5, 5);
        tl_2.setMsgMargin(5, 0, 10);
    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(context, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(context, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
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
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
