package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.adapter.BaseSlidingAdapter;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.flyco.tablayout.widget.ScaleInTransformer;
import com.flyco.tablayoutsamples.R;
import com.flyco.tablayoutsamples.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlidingTabActivity extends AppCompatActivity implements OnTabSelectListener {
    private Context mContext = this;
    private List<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab);

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }


        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        ViewPager2 viewPager2 = findViewById(R.id.vp2);

        viewPager2.setAdapter(new ViewPager2Adapter(mContext, Arrays.asList(mTitles)));
        viewPager2.setOffscreenPageLimit(1);
        RecyclerView view = (RecyclerView) viewPager2.getChildAt(0);
        view.setPadding(80, 0, 80, 0);
        view.setClipToPadding(false);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new ScaleInTransformer());
//        transformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(transformer);


        /** 默认 */
        SlidingTabLayout tabLayout_1 = ViewFindUtils.find(decorView, R.id.tl_1);
        /**自定义部分属性*/
        SlidingTabLayout tabLayout_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        /** 字体加粗,大写 */
        SlidingTabLayout tabLayout_3 = ViewFindUtils.find(decorView, R.id.tl_3);
        /** tab固定宽度 */
        SlidingTabLayout tabLayout_4 = ViewFindUtils.find(decorView, R.id.tl_4);
        /** indicator固定宽度 */
        SlidingTabLayout tabLayout_5 = ViewFindUtils.find(decorView, R.id.tl_5);
        /** indicator圆 */
        SlidingTabLayout tabLayout_6 = ViewFindUtils.find(decorView, R.id.tl_6);
        /** indicator矩形圆角 */
        final SlidingTabLayout tabLayout_7 = ViewFindUtils.find(decorView, R.id.tl_7);
        /** indicator三角形 */
        SlidingTabLayout tabLayout_8 = ViewFindUtils.find(decorView, R.id.tl_8);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_9 = ViewFindUtils.find(decorView, R.id.tl_9);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_10 = ViewFindUtils.find(decorView, R.id.tl_10);

        tabLayout_1.setViewPager(vp);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(this);
        tabLayout_3.setViewPager(vp);
        tabLayout_4.setViewPager(vp);
        tabLayout_5.setViewPager(vp);
        tabLayout_6.setViewPager(vp);
        tabLayout_7.setViewPager(vp, mTitles);
        tabLayout_8.setViewPager(vp, mTitles, getSupportFragmentManager(), mFragments);
        tabLayout_9.setViewPager(vp);
        tabLayout_10.setViewPager2(viewPager2, mTitles);

        vp.setCurrentItem(0);

        tabLayout_1.showDot(4);
        tabLayout_3.showDot(4);
        tabLayout_2.showDot(4);

        tabLayout_2.showMsg(3, 5);
        tabLayout_2.setMsgMargin(3, 0, 10);
        MsgView rtv_2_3 = tabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tabLayout_2.showMsg(5, 5);
        tabLayout_2.setMsgMargin(5, 0, 10);

//        tabLayout_7.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//                mFragments.add(SimpleCardFragment.getInstance("后端"));
//                mAdapter.notifyDataSetChanged();
//                tabLayout_7.addNewTab("后端");
//            }
//        });
    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
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

    private class ViewPager2FragmentStateAdapter extends FragmentStateAdapter {

        public ViewPager2FragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return SimpleCardFragment.getInstance(mTitles[position]);
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }
    }

    private class ViewPager2Adapter extends BaseSlidingAdapter<RecyclerView.ViewHolder, String> {

        public ViewPager2Adapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public void onBindVH(RecyclerView.ViewHolder holder, String s, int position) {
            TextView tv = holder.itemView.findViewById(R.id.card_title_tv);
            tv.setText(s);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(mInflater.inflate(R.layout.fr_simple_card, parent, false)) {

            };
        }
    }

}
