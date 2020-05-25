package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnIconSetUrlListener;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.flyco.tablayoutsamples.R;
import com.flyco.tablayoutsamples.entity.TabEntity;
import com.flyco.tablayoutsamples.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CommonTabActivity extends AppCompatActivity {
    private Context                  mContext    = this;
    private List<Fragment>      mFragments  = new ArrayList<>();
    private List<SimpleCardFragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private List<TabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout_1;
    private CommonTabLayout mTabLayout_2;
    private CommonTabLayout mTabLayout_3;
    private CommonTabLayout mTabLayout_4;
    private CommonTabLayout mTabLayout_5;
    private CommonTabLayout mTabLayout_6;
    private CommonTabLayout mTabLayout_7;
    private CommonTabLayout mTabLayout_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tab);

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
            mFragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vp_2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        /** with nothing */
        mTabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tl_1);
        /** with ViewPager */
        mTabLayout_2 = ViewFindUtils.find(mDecorView, R.id.tl_2);
        /** with Fragments */
        mTabLayout_3 = ViewFindUtils.find(mDecorView, R.id.tl_3);
        /** indicator固定宽度 */
        mTabLayout_4 = ViewFindUtils.find(mDecorView, R.id.tl_4);
        /** indicator固定宽度 */
        mTabLayout_5 = ViewFindUtils.find(mDecorView, R.id.tl_5);
        /** indicator矩形圆角 */
        mTabLayout_6 = ViewFindUtils.find(mDecorView, R.id.tl_6);
        /** indicator三角形 */
        mTabLayout_7 = ViewFindUtils.find(mDecorView, R.id.tl_7);
        /** indicator圆角色块 */
        mTabLayout_8 = ViewFindUtils.find(mDecorView, R.id.tl_8);

        mTabLayout_1.setTabData(mTabEntities);
        //icon的url自己选择图片框架
        mTabLayout_1.setOnIconSetUrlListener(new OnIconSetUrlListener() {
            @Override
            public void onImageSet(ImageView iv, String iconUrl) {
                System.out.println("iconUrl = " + iconUrl);
                Glide.with(CommonTabActivity.this).load(iconUrl).into(iv);
            }
        });
        getIconUrl();

        tl_2();
        mTabLayout_3.setTabData(mTabEntities, getSupportFragmentManager(), R.id.fl_change, mFragments2);
        mTabLayout_4.setTabData(mTabEntities);
        mTabLayout_5.setTabData(mTabEntities);
        mTabLayout_6.setTabData(mTabEntities);
        mTabLayout_7.setTabData(mTabEntities);
        mTabLayout_8.setTabData(mTabEntities);

        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout_1.setCurrentTab(position);
                mTabLayout_2.setCurrentTab(position);
                mTabLayout_4.setCurrentTab(position);
                mTabLayout_5.setCurrentTab(position);
                mTabLayout_6.setCurrentTab(position);
                mTabLayout_7.setCurrentTab(position);
                mTabLayout_8.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout_8.setCurrentTab(2);
        mTabLayout_3.setCurrentTab(1);

        //显示未读红点
        mTabLayout_1.showDot(2);
        mTabLayout_3.showDot(1);
        mTabLayout_4.showDot(1);

        //两位数
        mTabLayout_2.showMsg(0, 55);
        mTabLayout_2.setMsgMargin(0, -5, 5);

        //三位数
        mTabLayout_2.showMsg(1, 100);
        mTabLayout_2.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        mTabLayout_2.showDot(2);
        MsgView rtv_2_2 = mTabLayout_2.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
        }

        //设置未读消息背景
        mTabLayout_2.showMsg(3, 5);
        mTabLayout_2.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = mTabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void getIconUrl() {
        ArrayList<String> selectedUrl = new ArrayList<>();
        ArrayList<String> unSelectedUrl = new ArrayList<>();
        for (int i = 0; i < mTabEntities.size(); i++) {
            selectedUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571238544283&di=6dd89322871544d25148cdbbc04b9b84&imgtype=0&src=http%3A%2F%2Flogocola.poooster.com%2Flogocola%2F1801%2FGithub-5.jpg");
            unSelectedUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571238544289&di=47d741ec9dea4aab1066886681ed995c&imgtype=0&src=http%3A%2F%2Fpic.51yuansu.com%2Fpic2%2Fcover%2F00%2F33%2F09%2F581113ffe1269_610.jpg");
        }
        mTabLayout_1.updateIconUrl(selectedUrl, unSelectedUrl);
    }

    Random mRandom = new Random();

    private void tl_2() {
        mTabLayout_2.setTabData(mTabEntities);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mTabLayout_2.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
