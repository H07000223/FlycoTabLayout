package com.flyco.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.FragmentChangeManager;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

public class SegmentTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private Context context;
    private String[] titles;
    private LinearLayout tabsContainer;
    private int currentTab;
    private int lastTab;
    private int tabCount;
    /** 用于绘制显示器 */
    private Rect indicatorRect = new Rect();
    private GradientDrawable indicatorDrawable = new GradientDrawable();
    private GradientDrawable rectDrawable = new GradientDrawable();

    private Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float tabPadding;
    private boolean tabSpaceEqual;
    private float tabWidth;

    /** indicator */
    private int indicatorColor;
    private float indicatorHeight;
    private float indicatorCornerRadius;
    private float indicatorMarginLeft;
    private float indicatorMarginTop;
    private float indicatorMarginRight;
    private float indicatorMarginBottom;
    private long indicatorAnimDuration;
    private boolean indicatorAnimEnable;
    private boolean indicatorBounceEnable;

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

    private int barColor;
    private int barStrokeColor;
    private float barStrokeWidth;

    private int h;

    /** anim */
    private ValueAnimator valueAnimator;
    private OvershootInterpolator interpolator = new OvershootInterpolator(0.8f);

    private FragmentChangeManager fragmentChangeManager;
    private float[] radiusArr = new float[8];

    public SegmentTabLayout(Context context) {
        this(context, null, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), lp, cp);
        valueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentTabLayout);

        indicatorColor = ta.getColor(R.styleable.SegmentTabLayout_tl_indicator_color, Color.parseColor("#222831"));
        indicatorHeight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_height, -1);
        indicatorCornerRadius = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_corner_radius, -1);
        indicatorMarginLeft = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_left, dp2px(0));
        indicatorMarginTop = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_top, 0);
        indicatorMarginRight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_right, dp2px(0));
        indicatorMarginBottom = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_bottom, 0);
        indicatorAnimEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_anim_enable, false);
        indicatorBounceEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_bounce_enable, true);
        indicatorAnimDuration = ta.getInt(R.styleable.SegmentTabLayout_tl_indicator_anim_duration, -1);

        dividerColor = ta.getColor(R.styleable.SegmentTabLayout_tl_divider_color, indicatorColor);
        dividerWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_divider_width, dp2px(1));
        dividerPadding = ta.getDimension(R.styleable.SegmentTabLayout_tl_divider_padding, 0);

        textsize = ta.getDimension(R.styleable.SegmentTabLayout_tl_textsize, sp2px(13f));
        textSelectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        textUnselectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textUnselectColor, indicatorColor);
        textBold = ta.getBoolean(R.styleable.SegmentTabLayout_tl_textBold, false);
        textAllCaps = ta.getBoolean(R.styleable.SegmentTabLayout_tl_textAllCaps, false);

        tabSpaceEqual = ta.getBoolean(R.styleable.SegmentTabLayout_tl_tab_space_equal, true);
        tabWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_width, dp2px(-1));
        tabPadding = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_padding, tabSpaceEqual || tabWidth > 0 ? dp2px(0) : dp2px(10));

        barColor = ta.getColor(R.styleable.SegmentTabLayout_tl_bar_color, Color.TRANSPARENT);
        barStrokeColor = ta.getColor(R.styleable.SegmentTabLayout_tl_bar_stroke_color, indicatorColor);
        barStrokeWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_bar_stroke_width, dp2px(1));

        ta.recycle();
    }

    public void setTabData(String[] titles) {
        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be NULL or EMPTY !");
        }

        this.titles = titles;

        notifyDataSetChanged();
    }

    /** 关联数据支持同时切换fragments */
    public void setTabData(String[] titles, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        fragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        setTabData(titles);
    }

    /** 更新数据 */
    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        this.tabCount = titles.length;
        View tabView;
        for (int i = 0; i < tabCount; i++) {
            tabView = View.inflate(context, R.layout.layout_tab_segment, null);
            tabView.setTag(i);
            addTab(i, tabView);
        }

        updateTabStyles();
    }

    /** 创建并添加tab */
    private void addTab(final int position, View tabView) {
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(titles[position]);

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (currentTab != position) {
                    setCurrentTab(position);
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
            View tabView = tabsContainer.getChildAt(i);
            tabView.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == currentTab ? textSelectColor : textUnselectColor);
            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
//            tv_tab_title.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
            if (textAllCaps) {
                tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
            }

            if (textBold) {
                tv_tab_title.getPaint().setFakeBoldText(textBold);
            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < tabCount; ++i) {
            View tabView = tabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? textSelectColor : textUnselectColor);
        }
    }

    private void calcOffset() {
        final View currentTabView = tabsContainer.getChildAt(this.currentTab);
        cp.left = currentTabView.getLeft();
        cp.right = currentTabView.getRight();

        final View lastTabView = tabsContainer.getChildAt(this.lastTab);
        lp.left = lastTabView.getLeft();
        lp.right = lastTabView.getRight();

//        Log.d("AAA", "lp--->" + lp.left + "&" + lp.right);
//        Log.d("AAA", "cp--->" + cp.left + "&" + cp.right);
        if (lp.left == cp.left && lp.right == cp.right) {
            invalidate();
        } else {
            valueAnimator.setObjectValues(lp, cp);
            if (indicatorBounceEnable) {
                valueAnimator.setInterpolator(interpolator);
            }

            if (indicatorAnimDuration < 0) {
                indicatorAnimDuration = indicatorBounceEnable ? 500 : 250;
            }
            valueAnimator.setDuration(indicatorAnimDuration);
            valueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = tabsContainer.getChildAt(this.currentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        indicatorRect.left = (int) left;
        indicatorRect.right = (int) right;

        if (!indicatorAnimEnable) {
            if (currentTab == 0) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                radiusArr[0] = indicatorCornerRadius;
                radiusArr[1] = indicatorCornerRadius;
                radiusArr[2] = 0;
                radiusArr[3] = 0;
                radiusArr[4] = 0;
                radiusArr[5] = 0;
                radiusArr[6] = indicatorCornerRadius;
                radiusArr[7] = indicatorCornerRadius;
            } else if (currentTab == tabCount - 1) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                radiusArr[0] = 0;
                radiusArr[1] = 0;
                radiusArr[2] = indicatorCornerRadius;
                radiusArr[3] = indicatorCornerRadius;
                radiusArr[4] = indicatorCornerRadius;
                radiusArr[5] = indicatorCornerRadius;
                radiusArr[6] = 0;
                radiusArr[7] = 0;
            } else {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                radiusArr[0] = 0;
                radiusArr[1] = 0;
                radiusArr[2] = 0;
                radiusArr[3] = 0;
                radiusArr[4] = 0;
                radiusArr[5] = 0;
                radiusArr[6] = 0;
                radiusArr[7] = 0;
            }
        } else {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
            radiusArr[0] = indicatorCornerRadius;
            radiusArr[1] = indicatorCornerRadius;
            radiusArr[2] = indicatorCornerRadius;
            radiusArr[3] = indicatorCornerRadius;
            radiusArr[4] = indicatorCornerRadius;
            radiusArr[5] = indicatorCornerRadius;
            radiusArr[6] = indicatorCornerRadius;
            radiusArr[7] = indicatorCornerRadius;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        indicatorRect.left = (int) p.left;
        indicatorRect.right = (int) p.right;
        invalidate();
    }

    private boolean isFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();

        if (indicatorHeight < 0) {
            indicatorHeight = height - indicatorMarginTop - indicatorMarginBottom;
        }

        if (indicatorCornerRadius < 0 || indicatorCornerRadius > indicatorHeight / 2) {
            indicatorCornerRadius = indicatorHeight / 2;
        }

        //draw rect
        rectDrawable.setColor(barColor);
        rectDrawable.setStroke((int) barStrokeWidth, barStrokeColor);
        rectDrawable.setCornerRadius(indicatorCornerRadius);
        rectDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        rectDrawable.draw(canvas);

        // draw divider
        if (!indicatorAnimEnable && dividerWidth > 0) {
            dividerPaint.setStrokeWidth(dividerWidth);
            dividerPaint.setColor(dividerColor);
            for (int i = 0; i < tabCount - 1; i++) {
                View tab = tabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), dividerPadding, paddingLeft + tab.getRight(), height - dividerPadding, dividerPaint);
            }
        }


        //draw indicator line
        if (indicatorAnimEnable) {
            if (isFirstDraw) {
                isFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }

        indicatorDrawable.setColor(indicatorColor);
        indicatorDrawable.setBounds(paddingLeft + (int) indicatorMarginLeft + indicatorRect.left,
                (int) indicatorMarginTop, (int) (paddingLeft + indicatorRect.right - indicatorMarginRight),
                (int) (indicatorMarginTop + indicatorHeight));
        indicatorDrawable.setCornerRadii(radiusArr);
        indicatorDrawable.draw(canvas);

    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        lastTab = this.currentTab;
        this.currentTab = currentTab;
        updateTabSelection(currentTab);
        if (fragmentChangeManager != null) {
            fragmentChangeManager.setFragments(currentTab);
        }
        if (indicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
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

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.indicatorCornerRadius = dp2px(indicatorCornerRadius);
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

    public void setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.indicatorAnimDuration = indicatorAnimDuration;
    }

    public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.indicatorAnimEnable = indicatorAnimEnable;
    }

    public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.indicatorBounceEnable = indicatorBounceEnable;
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

    public long getIndicatorAnimDuration() {
        return indicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return indicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return indicatorBounceEnable;
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

            setMsgMargin(position, 2, 2);

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

    /**
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
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

            lp.leftMargin = dp2px(leftPadding);
            lp.topMargin = h > 0 ? (int) (h - textHeight) / 2 - dp2px(bottomPadding) : dp2px(bottomPadding);

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
            }
        }
        super.onRestoreInstanceState(state);
    }

    class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint cp = new IndicatorPoint();
    private IndicatorPoint lp = new IndicatorPoint();

    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
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
