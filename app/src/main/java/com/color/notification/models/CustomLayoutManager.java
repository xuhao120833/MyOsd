package com.color.notification.models;


import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends LinearLayoutManager {

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private static class TopSmoothScroller extends LinearSmoothScroller {

        TopSmoothScroller(Context context) {
            super(context);
        }

        /**
         * 以下参数以LinearSmoothScroller解释
         * @param viewStart RecyclerView的top位置
         * @param viewEnd RecyclerView的bottom位置
         * @param boxStart Item的top位置
         * @param boxEnd Item的bottom位置
         * @param snapPreference 判断滑动方向的标识（The edge which the view should snap to when entering the visible
         *                       area. One of {@link #SNAP_TO_START}, {@link #SNAP_TO_END} or
         *                       {@link #SNAP_TO_END}.）
         * @return 移动偏移量
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return boxStart - viewStart;// 这里是关键，得到的就是置顶的偏移量
        }
    }
}
