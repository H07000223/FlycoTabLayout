# FlycoTabLayout
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FlycoTabLayout-green.svg?style=true)](https://android-arsenal.com/details/1/2756)

一个Android TabLayout库,目前有3个TabLayout

* SlidingTabLayout:参照[PagerSlidingTabStrip](https://github.com/jpardogo/PagerSlidingTabStrip)进行大量修改.
    * 新增部分属性
    * 新增支持多种Indicator显示器
    * 新增支持未读消息显示
    * 新增方法for懒癌患者
    
    ```java
        /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
        public void setViewPager(ViewPager vp, String[] titles)
        
        /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
        public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) 
    ```

* CommonTabLayout:不同于SlidingTabLayout对ViewPager依赖,它是一个不依赖ViewPager可以与其他控件自由搭配使用的TabLayout.
    * 支持多种Indicator显示器,以及Indicator动画
    * 支持未读消息显示
    * 支持Icon以及Icon位置
    * 新增方法for懒癌患者
    
    ```java
        /** 关联数据支持同时切换fragments */
        public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments)
    ```

* SegmentTabLayout

## Demo
![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_1.gif)

![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_2.gif)

![](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_3.gif)


>## Change Log

 > v2.0.0(2016-03-01)
   - 删除了对FlycoRoundView库的依赖
   - 新增方法getIconView和getTitleView(为了某些情况需要动态更新icon之类的)

 > v2.0.2(2016-04-23)
   - 删除了对NineOldAnimation库依赖(仅支持3.0+)

## Gradle

```groovy
dependencies{
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.nineoldandroids:library:2.4.0'
    compile 'com.flyco.roundview:FlycoRoundView_Lib:1.1.2@aar'
    compile 'com.flyco.tablayout:FlycoTabLayout_Lib:1.5.0@aar'
}

After v2.0.0(support 2.2+)
dependencies{
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.nineoldandroids:library:2.4.0'
    compile 'com.flyco.tablayout:FlycoTabLayout_Lib:2.0.0@aar'
}

After v2.0.2(support 3.0+)
dependencies{
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
}
```

## Attributes

|name|format|description|
|:---:|:---:|:---:|
| tl_indicator_color | color |设置显示器颜色
| tl_indicator_height | dimension |设置显示器高度
| tl_indicator_width | dimension |设置显示器固定宽度
| tl_indicator_margin_left | dimension |设置显示器margin,当indicator_width大于0,无效
| tl_indicator_margin_top | dimension |设置显示器margin,当indicator_width大于0,无效
| tl_indicator_margin_right | dimension |设置显示器margin,当indicator_width大于0,无效
| tl_indicator_margin_bottom | dimension |设置显示器margin,当indicator_width大于0,无效 
| tl_indicator_corner_radius | dimension |设置显示器圆角弧度
| tl_indicator_gravity | enum |设置显示器上方(TOP)还是下方(BOTTOM),只对常规显示器有用
| tl_indicator_style | enum |设置显示器为常规(NORMAL)或三角形(TRIANGLE)或背景色块(BLOCK)
| tl_underline_color | color |设置下划线颜色
| tl_underline_height | dimension |设置下划线高度
| tl_underline_gravity | enum |设置下划线上方(TOP)还是下方(BOTTOM)
| tl_divider_color | color |设置分割线颜色
| tl_divider_width | dimension |设置分割线宽度
| tl_divider_padding |dimension| 设置分割线的paddingTop和paddingBottom
| tl_tab_padding |dimension| 设置tab的paddingLeft和paddingRight
| tl_tab_space_equal |boolean| 设置tab大小等分
| tl_tab_width |dimension| 设置tab固定大小
| tl_textsize |dimension| 设置字体大小
| tl_textSelectColor |color| 设置字体选中颜色
| tl_textUnselectColor |color| 设置字体未选中颜色
| tl_textBold |boolean| 设置字体加粗
| tl_iconWidth |dimension| 设置icon宽度(仅支持CommonTabLayout)
| tl_iconHeight |dimension|设置icon高度(仅支持CommonTabLayout)
| tl_iconVisible |boolean| 设置icon是否可见(仅支持CommonTabLayout)
| tl_iconGravity |enum| 设置icon显示位置,对应Gravity中常量值,左上右下(仅支持CommonTabLayout)
| tl_iconMargin |dimension| 设置icon与文字间距(仅支持CommonTabLayout)
| tl_indicator_anim_enable |boolean| 设置显示器支持动画(only for CommonTabLayout)
| tl_indicator_anim_duration |integer| 设置显示器动画时间(only for CommonTabLayout)
| tl_indicator_bounce_enable |boolean| 设置显示器支持动画回弹效果(only for CommonTabLayout)
| tl_indicator_width_equal_title |boolean| 设置显示器与标题一样长(only for SlidingTabLayout)

## Dependence
*   [NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)
*   [FlycoRoundView](https://github.com/H07000223/FlycoRoundView)

## Thanks
*   [PagerSlidingTabStrip](https://github.com/jpardogo/PagerSlidingTabStrip)
