package com.flyco.tablayoutsamples.ui

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayoutsamples.R
import com.flyco.tablayoutsamples.entity.TabEntity
import java.util.*

/**
 * Created by zengwendi on 2018/4/26.
 */

class SildingActivity : AppCompatActivity() {

    private val mFragments = ArrayList<Fragment>()
    private var mAdapter: MyPagerAdapter? = null
    private val mTitles = arrayOf("自动投标", "散标", "债转")
    private val mTabEntities = ArrayList<CustomTabEntity>()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sliding)
        val tabEntitys = mTitles.mapTo(destination = ArrayList<CustomTabEntity>()) {
            TabEntity(it, 0, 0)
        }
        var tab = findViewById(R.id.tl_6) as CommonTabLayout
        var tab7 = findViewById(R.id.tl_7) as CommonTabLayout

        tab.setTabData(tabEntitys)
        tab7.setTabData(tabEntitys)

    }

    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }
    }
}
