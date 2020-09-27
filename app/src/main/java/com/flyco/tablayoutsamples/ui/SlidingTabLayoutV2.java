package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.flyco.tablayout.SlidingTabLayout;

public class SlidingTabLayoutV2 extends SlidingTabLayout {
    public SlidingTabLayoutV2(Context context) {
        super(context);
    }

    public SlidingTabLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingTabLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private Drawable mIndicatorDrawable = new BitmapDrawable();

    public void setmIndicatorDrawable(Drawable mIndicatorDrawable) {
        this.mIndicatorDrawable = mIndicatorDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mIndicatorDrawable.setBounds(getPaddingLeft() + (int) getIndicatorMarginLeft() + mIndicatorRect.left,
                getHeight() - 50,
                getPaddingLeft() + mIndicatorRect.right - (int) getIndicatorMarginRight(),
                getHeight() - (int) getIndicatorMarginBottom());

        mIndicatorDrawable.draw(canvas);
        super.onDraw(canvas);

    }
}
