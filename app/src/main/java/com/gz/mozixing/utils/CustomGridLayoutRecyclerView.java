package com.gz.mozixing.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by davy on 2017/7/14.
 */

public class CustomGridLayoutRecyclerView extends RecyclerView {
    StaggeredGridLayoutManager layoutManager;
    public CustomGridLayoutRecyclerView(Context context) {
        super(context);
    }

    public CustomGridLayoutRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridLayoutRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int defaultsize=measureHight(Integer.MAX_VALUE >> 2, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(defaultsize, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
        */

        //  AT_MOST参数表示控件可以自由调整大小，最大不超过Integer.MAX_VALUE/4
       // int height= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        //  AT_MOST参数表示控件可以自由调整大小，最大不超过Integer.MAX_VALUE/4
        int height= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }



   /* @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        this.layoutManager=(StaggeredGridLayoutManager)layout;
    }

    int heightMode;
    private int measureHight(int size, int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = size;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }



    *//**
     * 判断RecyclerView是否滑动到了底部。
     *
     * @return True表示滑动到了底部，False表示没有滑动到底部。
     *//*
    private boolean isBottom() {
        int[] items = layoutManager.findLastVisibleItemPositions(null);
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount > 0 && (items[0] == totalItemCount - 1 || items[1] == totalItemCount - 1);
    }

    public boolean isTop() {
        int[] firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPositions(null);
        int x=0;
        for(int i=0;i<firstVisibleItem.length;i++){
            if (x==firstVisibleItem[i]){
                View firstchild = getChildAt(0);
                if (firstchild.getTop() == getPaddingTop()) {
                    return true;
                }
            }
        }
        return false;
    }

    float down = 0;
    float y;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down = event.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if(heightMode == MeasureSpec.AT_MOST){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {
                    y = event.getRawY();
                    if (y > down) {
                        if (isTop()) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (y < down) {
                        if (isBottom()) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }

                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

*/
}
