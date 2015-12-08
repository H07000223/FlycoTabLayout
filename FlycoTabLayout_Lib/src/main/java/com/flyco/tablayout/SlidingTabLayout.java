package com.flyco.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;

import java.util.ArrayList;

/** 滑动TabLayout,对于ViewPager的依赖性强 */
public class SlidingTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private Context context;
    private ViewPager vp;
    private String[] titles;
    private LinearLayout tabsContainer;
    private int currentTab;
    private float currentPositionOffset;
    private int tabCount;
    /** 用于绘制显示器 */
    private Rect indicatorRect = new Rect();
    /** 用于实现滚动居中 */
    private Rect tabRect = new Rect();
    private GradientDrawable indicatorDrawable = new GradientDrawable();

    private Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path trianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int indicatorStyle = STYLE_NORMAL;

    private float tabPadding;
    private boolean tabSpaceEqual;
    private float tabWidth;

    /** indicator */
    private int indicatorColor;
    private float indicatorHeight;
    private float indicatorWidth;
    private float indicatorCornerRadius;
    private float indicatorMarginLeft;
    private float indicatorMarginTop;
    private float indicatorMarginRight;
    private float indicatorMarginBottom;
    private int indicatorGravity;
    private boolean indicatorWidthEqualTitle;

    /** underline */
    private int underlineColor;
    private float underlineHeight;
    private int underlineGravity;

    /** divider */
    private int dividerColor;
    private float dividerWidth;
    private float dividerPadding;

    /** title */
    private float textsize;
    private int textSelectColor;
    private int textUnselectColor;
    private boolean textBold;
    private boolean textAllCaps;

    private int lastScrollX;
    private int h;

    public SlidingTabLayout(Context context) {
        this(context, null, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.context = context;
        tabsContainer = new LinearLayout(context);
        addView(tabsContainer);

        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            h = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);

        indicatorStyle = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_style, STYLE_NORMAL);
        indicatorColor = ta.getColor(R.styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(indicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        indicatorHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_height,
                dp2px(indicatorStyle == STYLE_TRIANGLE ? 4 : (indicatorStyle == STYLE_BLOCK ? -1 : 2)));
        indicatorWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_width, dp2px(indicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        indicatorCornerRadius = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_corner_radius, dp2px(indicatorStyle == STYLE_BLOCK ? -1 : 0));
        indicatorMarginLeft = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_left, dp2px(0));
        indicatorMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_top, dp2px(indicatorStyle == STYLE_BLOCK ? 7 : 0));
        indicatorMarginRight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_right, dp2px(0));
        indicatorMarginBottom = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_bottom, dp2px(indicatorStyle == STYLE_BLOCK ? 7 : 0));
        indicatorGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_gravity, Gravity.BOTTOM);
        indicatorWidthEqualTitle = ta.getBoolean(R.styleable.SlidingTabLayout_tl_indicator_width_equal_title, false);

        underlineColor = ta.getColor(R.styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        underlineHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_underline_height, dp2px(0));
        underlineGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_underline_gravity, Gravity.BOTTOM);

        dividerColor = ta.getColor(R.styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        dividerWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_width, dp2px(0));
        dividerPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_padding, dp2px(12));

        textsize = ta.getDimension(R.styleable.SlidingTabLayout_tl_textsize, sp2px(14));
        textSelectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        textUnselectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
        textBold = ta.getBoolean(R.styleable.SlidingTabLayout_tl_textBold, false);
        textAllCaps = ta.getBoolean(R.styleable.SlidingTabLayout_tl_textAllCaps, false);

        tabSpaceEqual = ta.getBoolean(R.styleable.SlidingTabLayout_tl_tab_space_equal, false);
        tabWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_width, dp2px(-1));
        tabPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_padding, tabSpaceEqual || tabWidth > 0 ? dp2px(0) : dp2px(20));

        ta.recycle();
    }

    /** 关联ViewPager */
    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.vp = vp;

        this.vp.removeOnPageChangeListener(this);
        this.vp.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
    public void setViewPager(ViewPager vp, String[] titles) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        if (titles.length != vp.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }

        this.vp = vp;
        this.titles = titles;

        this.vp.removeOnPageChangeListener(this);
        this.vp.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
    public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        this.vp = vp;
        this.vp.setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));

        this.vp.removeOnPageChangeListener(this);
        this.vp.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /** 更新数据 */
    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        this.tabCount = titles == null ? vp.getAdapter().getCount() : titles.length;
        View tabView;
        for (int i = 0; i < tabCount; i++) {
            if (vp.getAdapter() instanceof CustomTabProvider) {
                tabView = ((CustomTabProvider) vp.getAdapter()).getCustomTabView(this, i);
            } else {
                tabView = View.inflate(context, R.layout.layout_tab, null);
            }

            CharSequence pageTitle = titles == null ? vp.getAdapter().getPageTitle(i) : titles[i];
            addTab(i, pageTitle.toString(), tabView);
        }

        updateTabStyles();
    }

    /** 创建并添加tab */
    private void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) tv_tab_title.setText(title);
        }

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp.getCurrentItem() != position) {
                    vp.setCurrentItem(position);
                    if (listener != null) {
                        listener.onTabSelect(position);
                    }
                } else {
                    if (listener != null) {
                        listener.onTabReselect(position);
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = tabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (tabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) tabWidth, LayoutParams.MATCH_PARENT);
        }

        tabsContainer.addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
//            v.setPadding((int) tabPadding, v.getPaddingTop(), (int) tabPadding, v.getPaddingBottom());
            TextView tv_tab_title = (TextView) v.findViewById(R.id.tv_tab_title);
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == currentTab ? textSelectColor : textUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
                tv_tab_title.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
                if (textAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (textBold) {
                    tv_tab_title.getPaint().setFakeBoldText(textBold);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * currentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.currentTab = position;
        this.currentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /** HorizontalScrollView滚到当前tab,并且居中显示 */
    private void scrollToCurrentTab() {
        if (tabCount <= 0) {
            return;
        }

        int offset = (int) (currentPositionOffset * tabsContainer.getChildAt(currentTab).getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = tabsContainer.getChildAt(currentTab).getLeft() + offset;

        if (currentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((tabRect.right - tabRect.left) / 2);
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < tabCount; ++i) {
            View tabView = tabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);

            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? textSelectColor : textUnselectColor);
            }

            if (vp.getAdapter() instanceof CustomTabProvider) {
                if (isSelect) {
                    ((CustomTabProvider) vp.getAdapter()).tabSelect(tabView);
                } else {
                    ((CustomTabProvider) vp.getAdapter()).tabUnselect(tabView);
                }
            }
        }
    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = tabsContainer.getChildAt(this.currentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for indicatorWidthEqualTitle
        if (indicatorStyle == STYLE_NORMAL && indicatorWidthEqualTitle) {
            TextView tab_title = (TextView) currentTabView.findViewById(R.id.tv_tab_title);
            textPaint.setTextSize(textsize);
            float textWidth = textPaint.measureText(tab_title.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        if (this.currentTab < tabCount - 1) {
            View nextTabView = tabsContainer.getChildAt(this.currentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + currentPositionOffset * (nextTabLeft - left);
            right = right + currentPositionOffset * (nextTabRight - right);

            //for indicatorWidthEqualTitle
            if (indicatorStyle == STYLE_NORMAL && indicatorWidthEqualTitle) {
                TextView next_tab_title = (TextView) nextTabView.findViewById(R.id.tv_tab_title);
                textPaint.setTextSize(textsize);
                float nextTextWidth = textPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + currentPositionOffset * (nextMargin - margin);
            }
        }

        indicatorRect.left = (int) left;
        indicatorRect.right = (int) right;
        //for indicatorWidthEqualTitle
        if (indicatorStyle == STYLE_NORMAL && indicatorWidthEqualTitle) {
            indicatorRect.left = (int) (left + margin - 1);
            indicatorRect.right = (int) (right - margin - 1);
        }

        tabRect.left = (int) left;
        tabRect.right = (int) right;

        if (indicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - indicatorWidth) / 2;

            if (this.currentTab < tabCount - 1) {
                View nextTab = tabsContainer.getChildAt(this.currentTab + 1);
                indicatorLeft = indicatorLeft + currentPositionOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
            }

            indicatorRect.left = (int) indicatorLeft;
            indicatorRect.right = (int) (indicatorRect.left + indicatorWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (dividerWidth > 0) {
            dividerPaint.setStrokeWidth(dividerWidth);
            dividerPaint.setColor(dividerColor);
            for (int i = 0; i < tabCount - 1; i++) {
                View tab = tabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), dividerPadding, paddingLeft + tab.getRight(), height - dividerPadding, dividerPaint);
            }
        }

        // draw underline
        if (underlineHeight > 0) {
            rectPaint.setColor(underlineColor);
            if (underlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - underlineHeight, tabsContainer.getWidth() + paddingLeft, height, rectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, tabsContainer.getWidth() + paddingLeft, underlineHeight, rectPaint);
            }
        }

        //draw indicator line

        calcIndicatorRect();
        if (indicatorStyle == STYLE_TRIANGLE) {
            if (indicatorHeight > 0) {
                trianglePaint.setColor(indicatorColor);
                trianglePath.reset();
                trianglePath.moveTo(paddingLeft + indicatorRect.left, height);
                trianglePath.lineTo(paddingLeft + indicatorRect.left / 2 + indicatorRect.right / 2, height - indicatorHeight);
                trianglePath.lineTo(paddingLeft + indicatorRect.right, height);
                trianglePath.close();
                canvas.drawPath(trianglePath, trianglePaint);
            }
        } else if (indicatorStyle == STYLE_BLOCK) {
            if (indicatorHeight < 0) {
                indicatorHeight = height - indicatorMarginTop - indicatorMarginBottom;
            } else {

            }

            if (indicatorHeight > 0) {
                if (indicatorCornerRadius < 0 || indicatorCornerRadius > indicatorHeight / 2) {
                    indicatorCornerRadius = indicatorHeight / 2;
                }

                indicatorDrawable.setColor(indicatorColor);
                indicatorDrawable.setBounds(paddingLeft + (int) indicatorMarginLeft + indicatorRect.left,
                        (int) indicatorMarginTop, (int) (paddingLeft + indicatorRect.right - indicatorMarginRight),
                        (int) (indicatorMarginTop + indicatorHeight));
                indicatorDrawable.setCornerRadius(indicatorCornerRadius);
                indicatorDrawable.draw(canvas);
            }
        } else {
               /* rectPaint.setColor(indicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + indicatorRect.left, getHeight() - indicatorHeight,
                        indicatorRect.right + getPaddingLeft(), getHeight(), rectPaint);*/

            if (indicatorHeight > 0) {
                indicatorDrawable.setColor(indicatorColor);

                if (indicatorGravity == Gravity.BOTTOM) {
                    indicatorDrawable.setBounds(paddingLeft + (int) indicatorMarginLeft + indicatorRect.left,
                            height - (int) indicatorHeight - (int) indicatorMarginBottom,
                            paddingLeft + indicatorRect.right - (int) indicatorMarginRight,
                            height - (int) indicatorMarginBottom);
                } else {
                    indicatorDrawable.setBounds(paddingLeft + (int) indicatorMarginLeft + indicatorRect.left,
                            (int) indicatorMarginTop,
                            paddingLeft + indicatorRect.right - (int) indicatorMarginRight,
                            (int) indicatorHeight + (int) indicatorMarginTop);
                }
                indicatorDrawable.setCornerRadius(indicatorCornerRadius);
                indicatorDrawable.draw(canvas);
            }
        }
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        vp.setCurrentItem(currentTab);
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.indicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.tabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.tabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.tabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.indicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.indicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.indicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.indicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.indicatorMarginTop = dp2px(indicatorMarginTop);
        this.indicatorMarginRight = dp2px(indicatorMarginRight);
        this.indicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.indicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.underlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.underlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.dividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.dividerPadding = dp2px(dividerPadding);
        invalidate();
    }

    public void setTextsize(float textsize) {
        this.textsize = sp2px(textsize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.textUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
        updateTabStyles();
    }


    public int getTabCount() {
        return tabCount;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public int getIndicatorStyle() {
        return indicatorStyle;
    }

    public float getTabPadding() {
        return tabPadding;
    }

    public boolean isTabSpaceEqual() {
        return tabSpaceEqual;
    }

    public float getTabWidth() {
        return tabWidth;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public float getIndicatorWidth() {
        return indicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return indicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return indicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return indicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return indicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return indicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public float getUnderlineHeight() {
        return underlineHeight;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public float getDividerWidth() {
        return dividerWidth;
    }

    public float getDividerPadding() {
        return dividerPadding;
    }

    public float getTextsize() {
        return textsize;
    }

    public int getTextSelectColor() {
        return textSelectColor;
    }

    public int getTextUnselectColor() {
        return textUnselectColor;
    }

    public boolean isTextBold() {
        return textBold;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    //setter and getter

    // show MsgTipView
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> initSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= tabCount) {
            position = tabCount - 1;
        }

        View tabView = tabsContainer.getChildAt(position);
        RoundTextView tipView = (RoundTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (initSetMap.get(position) != null && initSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, 4, 2);
            initSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= tabCount) {
            position = tabCount - 1;
        }
        showMsg(position, 0);
    }

    /** 隐藏未读消息 */
    public void hideMsg(int position) {
        if (position >= tabCount) {
            position = tabCount - 1;
        }

        View tabView = tabsContainer.getChildAt(position);
        RoundTextView tipView = (RoundTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /** 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳 */
    public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
        if (position >= tabCount) {
            position = tabCount - 1;
        }
        View tabView = tabsContainer.getChildAt(position);
        RoundTextView tipView = (RoundTextView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            textPaint.setTextSize(textsize);
            float textWidth = textPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = textPaint.descent() - textPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();
            lp.leftMargin = tabWidth >= 0 ? (int) (tabWidth / 2 + textWidth / 2 + dp2px(leftPadding)) : (int) (tabPadding + textWidth + dp2px(leftPadding));
            lp.topMargin = h > 0 ? (int) (h - textHeight) / 2 - dp2px(bottomPadding) : 0;
            tipView.setLayoutParams(lp);
        }
    }

    /** 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取RoundTextView对象从而各种设置 */
    public RoundTextView getMsgView(int position) {
        if (position >= tabCount) {
            position = tabCount - 1;
        }
        View tabView = tabsContainer.getChildAt(position);
        RoundTextView tipView = (RoundTextView) tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener listener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.listener = listener;
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
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

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    public interface CustomTabProvider {
        View getCustomTabView(ViewGroup parent, int position);

        void tabSelect(View tab);

        void tabUnselect(View tab);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("currentTab", currentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentTab = bundle.getInt("currentTab");
            state = bundle.getParcelable("instanceState");
            if (currentTab != 0 && tabsContainer.getChildCount() > 0) {
                updateTabSelection(currentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
