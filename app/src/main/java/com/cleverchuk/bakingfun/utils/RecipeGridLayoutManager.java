package com.cleverchuk.bakingfun.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

/**
 * Idea taken from:
 * Author: Diep Nguyen
 * Source: https://codentrick.com/part-4-android-recyclerview-grid/
 * a subclass of {@link GridLayoutManager} this class
 * auto fits the number of columns based on screen size
 * Created by chuk on 4/30/18,at 22:47.
 */

public class RecipeGridLayoutManager extends GridLayoutManager {

    private int width;
    private boolean isWidthChanged = true;

    public RecipeGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        setWidth(getColumnWidth(context, spanCount));
    }

    public RecipeGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (isWidthChanged && width > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingLeft() - getPaddingRight();
            } else {
                totalSpace = getHeight() - getPaddingBottom() - getPaddingTop();
            }

            int spanCount = Math.max(1, totalSpace / width);
            setSpanCount(spanCount);
            isWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }

    private int getColumnWidth(Context context, int spanCount) {
        int width = spanCount;
        if (spanCount <= 0) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    context.getResources().getDisplayMetrics());
        }
        return width;
    }

    private void setWidth(int newWidth) {
        if (newWidth > 0 && newWidth != width)
        {
            width = newWidth;
            isWidthChanged = true;
        }
    }


}


