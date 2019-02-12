package com.gz.mozixing.utils.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author Alex
 * @since 19/1/30
 */
public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;
    // 上一次触摸时的y坐标
    private float mPrevy;

    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                mPrevy = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                final float eventy = event.getY();
                float xDiff = Math.abs(eventX - mPrevX);
                float yDiff = Math.abs(eventy - mPrevy);
                // Log.d("refresh" ,"move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                Log.d("SwipeRefreshLayout", "xDiff=" + xDiff + "  yDiff=" + yDiff + "  mTouchSlop=" + mTouchSlop);
                if (xDiff > mTouchSlop && yDiff < 200) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }
}