#Change Log
Version 1.1.2 *(2015-10-14)*
----------------------------
* add method setViewPager(ViewPager vp, String[] titles)  for the condition that you do not want set titles in page adapter 

Version 1.1.4 *(2015-10-16)*
----------------------------
* fix bug: indicator not show if you do not call viewpager's setCurrentItem method during initialization.

Version 1.1.6 *(2015-10-18)*
----------------------------
* add method setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments)
  for the condition that you even do not want to instantiate page adapter by yourself
* add listener OnTabSelectedListener

Version 1.1.8 *(2015-10-19)*
----------------------------
* add block indicator

Version 1.2.0 *(2015-10-20)*
----------------------------
* add unread msg dot (TipView)

Version 1.3.0 *(2015-10-22)*
----------------------------
* new added View: CommonTabLayout is a tablayout without dependence of ViewPager

Version 1.3.2 *(2015-10-28)*
----------------------------
* extract common attributes

Version 1.3.4 *(2015-11-5)*
----------------------------
* replace TipView with RoundTextView

Version 1.3.6 *(2015-11-10)*
----------------------------
* new add attr tl_indicator_width_equal_title

Version 1.4.0 *(2015-11-11)*
----------------------------
* fix bug: attr tl_indicator_width_equal_title sometime invalid

Version 1.4.2 *(2015-12-9)*
----------------------------
* new added tablayout

Version 1.4.4 *(2015-12-11)*
----------------------------
* fix bug: CommonTabLayout first setCurrentTab() cause indicator not show

Version 1.4.6 *(2015-12-11)*
----------------------------
* fix bug: CommonTabLayout first setCurrentTab() cause indicator not show

Version 1.5.0 *(2015-12-11)*
----------------------------
* change code style

Version 2.0.0 *(2016.3.1)*
----------------------------
* remove the dependence of FlycoRoundView
* new added method getIconView and getTitleView

Version 2.0.2 *(2016.4.23)*
----------------------------
* remove the dependence of NineOldAnimation(only support 3.0+)

Version 2.0.6 *(2016.5.21)*
----------------------------
* remove CustomTabProvider in SlidingTabLayout
* new added method 'addNewTab(String title)' for SlidingTabLayout

Version 2.0.8 *(2016.7.26)*
---------------------------
* Fix #27，#31(new added method 'setCurrentTab(int currentTab, boolean smoothScroll)' for SlidingTabLayout and redefine attr 'tl_textBold')

Version 2.1.0 *(2016.10.28)*
----------------------------
* remove Application label in manifest
* add method to make viewpager snap on tab click

Version 2.1.2 *(2017.1.23)*
----------------------------
* update compileSdkVersion to 25, buildToolsVersion to "25.0.2"


