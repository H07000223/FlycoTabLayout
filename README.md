# FlycoTabLayout
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FlycoTabLayout-green.svg?style=true)](https://android-arsenal.com/details/1/2756)
#### [中文版](https://github.com/H07000223/FlycoTabLayout/blob/master/README_CN.md)
An Android TabLayout Lib has 3 kinds of TabLayout at present.

* SlidingTabLayout: deeply modified from [PagerSlidingTabStrip](https://github.com/jpardogo/PagerSlidingTabStrip).
    * new added attribute
    * new added kinds of indicators
    * new added unread msg tip
    * new added method for convenience
    
    ```java
        /** no need to set titles in adapter */
        public void setViewPager(ViewPager vp, String[] titles)
        
        /** no need to initialize even adapter */
        public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) 
    ```

* CommonTabLayout:unlike SlidingTabLayout's dependence on ViewPager,it is a tabLayout without dependence on ViewPager and 
can be used freely with other widgets together.
    * support kinds of indicators and indicator animation
    * support unread msg tip
    * support icon and icon gravity.
    * new added method for convenience
    
    ```java
        /** support switch fragments itself */
        public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments)
    ```

* SegmentTabLayout

## Demo
![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_1.gif)

![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_2.gif)

![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_3.gif)


## Change Log

 > 3.0.0 (2021-09-30)
   - upgrade to AndroidX
   - publish on Maven Central


## Gradle

```groovy
// AndroidX
dependencies {
    implementation 'io.github.h07000223:flycoTabLayout:3.0.0'
}

// Old
dependencies{
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
}
```

## Attributes

|name|format|description|
|:---:|:---:|:---:|
| tl_indicator_color | color |set indicator color
| tl_indicator_height | dimension |set indicator height
| tl_indicator_width | dimension |set indicator width
| tl_indicator_margin_left | dimension |set indicator margin,invalid when indicator width is greater than 0.
| tl_indicator_margin_top | dimension |set indicator margin,invalid when indicator width is greater than 0.
| tl_indicator_margin_right | dimension |set indicator margin,invalid when indicator width is greater than 0.
| tl_indicator_margin_bottom | dimension |set indicator margin,invalid when indicator width is greater than 0.
| tl_indicator_corner_radius | dimension |set indicator corner radius
| tl_indicator_gravity | enum |set indicator gravity TOP or BOTTOM.
| tl_indicator_style | enum |set indicator style NORMAL or TRIANGLE or BLOCK
| tl_underline_color | color |set underline color
| tl_underline_height | dimension |set underline height
| tl_underline_gravity | enum |set underline gravity TOP or BOTTOM
| tl_divider_color | color |set divider color
| tl_divider_width | dimension |set divider width
| tl_divider_padding |dimension| set divider paddingTop and paddingBottom
| tl_tab_padding |dimension| set tab paddingLeft and paddingRight
| tl_tab_space_equal |boolean| set tab space equal
| tl_tab_width |dimension| set tab width
| tl_textsize |dimension| set text size
| tl_textSelectColor |color| set text select color
| tl_textUnselectColor |color|  set text unselect color
| tl_textBold |boolean| set text is bold 
| tl_iconWidth |dimension| set icon width(only for CommonTabLayout)
| tl_iconHeight |dimension|set icon height(only for CommonTabLayout)
| tl_iconVisible |boolean| set icon is visible(only for CommonTabLayout)
| tl_iconGravity |enum| set icon gravity LEFT or TOP or RIGHT or BOTTOM(only for CommonTabLayout)
| tl_iconMargin |dimension| set icon margin with text(only for CommonTabLayout)
| tl_indicator_anim_enable |boolean| set indicator support animation(only for CommonTabLayout)
| tl_indicator_anim_duration |integer| set indicator animation duration(only for CommonTabLayout)
| tl_indicator_bounce_enable |boolean| set indicator aniamtion with bounce effect(only for CommonTabLayout)
| tl_indicator_width_equal_title |boolean| set indicator width same as text(only for SlidingTabLayout)

## Dependence
*   [NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)
*   [FlycoRoundView](https://github.com/H07000223/FlycoRoundView)

## Thanks
*   [PagerSlidingTabStrip](https://github.com/jpardogo/PagerSlidingTabStrip)
