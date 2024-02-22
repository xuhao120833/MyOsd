package com.color.notification.models;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

/***
 * Author:徐豪
 * Time:2024-2-18
 * 用于实现RecyclerView Item侧滑显示菜单功能
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback implements Instance {

    public ItemTouchHelperCallback() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0; // 拖动标志
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; // 滑动标志，这里允许左右滑动
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    //是否支持侧滑
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 拖动操作，暂不处理
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        try {
            // 这里可以监听到子视图的侧滑操作
            int swipedPosition = viewHolder.getAdapterPosition();

            Log.d("CustomRecyclerView调用顺序", " onSwiped ");
            if (direction == ItemTouchHelper.LEFT) {
                // 向左侧滑
                Log.d("CustomRecyclerView", " 向左侧滑 " + swipedPosition);
                //如果是蓝牙通知还需复位标志位
                if (StaticVariableUtils.list.get(swipedPosition).lanya) {
                    Log.d("CustomRecyclerView", " 蓝牙进度条向右滑动 ");
                    StaticVariableUtils.lanya_first_accept_android_text = true;
                    StaticVariableUtils.lanya_number = -1;
                    StaticVariableUtils.android_lanya_progress = -1;
                    StaticVariableUtils.lanya_first_transmit = false;
                    StaticVariableUtils.notification_has_lanya = false;
                }
                StaticVariableUtils.list.remove(swipedPosition);
                STATIC_INSTANCE_UTILS.notificationCenterAdapter.notifyItemRemoved(swipedPosition);
                if (StaticVariableUtils.list.size() == 0) {
                    STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                }
            }
            if (direction == ItemTouchHelper.RIGHT) {
                // 向右侧滑
                Log.d("CustomRecyclerView", " 向右侧滑 " + swipedPosition);
                //如果是蓝牙通知还需复位标志位
                if (StaticVariableUtils.list.get(swipedPosition).lanya) {
                    Log.d("CustomRecyclerView", " 蓝牙进度条向右滑动 ");
                    StaticVariableUtils.lanya_first_accept_android_text = true;
                    StaticVariableUtils.lanya_number = -1;
                    StaticVariableUtils.android_lanya_progress = -1;
                    StaticVariableUtils.lanya_first_transmit = false;
                    StaticVariableUtils.notification_has_lanya = false;
                }
                StaticVariableUtils.list.remove(swipedPosition);
                STATIC_INSTANCE_UTILS.notificationCenterAdapter.notifyItemRemoved(swipedPosition);
                if (StaticVariableUtils.list.size() == 0) {
                    STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        try {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                int swipedPosition = viewHolder.getAdapterPosition();

                //蓝牙通知没有菜单，不需要显示，只需要支持左右滑动删除即可
                if (StaticVariableUtils.list.get(swipedPosition).lanya) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else {

                    Notification_Center_Adapter.Center_ViewHolder center_viewHolder = (Notification_Center_Adapter.Center_ViewHolder) viewHolder;
                    // 当用户正在滑动 Item 时
                    Log.d("CustomRecyclerView调用顺序", " onChildDraw ");
                    // 根据滑动距离 dX 判断是否需要显示底层菜单
                    Log.d("CustomRecyclerView", " 滑动d X " + dX);
                    Log.d("CustomRecyclerView", " 滑动d Y " + dY);
                    // 获取 RecyclerView 的宽度
                    int recyclerViewWidth = recyclerView.getWidth();

                    Log.d("CustomRecyclerView", " notification_center_item_settings.getWidth()  " + center_viewHolder.notification_center_item_settings.getWidth());
                    Log.d("CustomRecyclerView", " notification_center_item.getWidth()  " + center_viewHolder.notification_center_item.getWidth());
                    Log.d("CustomRecyclerView", " recyclerViewWidth  " + recyclerViewWidth);
                    if (dX > 0 && center_viewHolder.isSwipemenu) {
                        center_viewHolder.notification_center_item_settings.setVisibility(View.INVISIBLE);
                        STATIC_INSTANCE_UTILS.manimationManager.Notification_center_item_slideright(center_viewHolder.notification_center_item);
                        center_viewHolder.isSwipemenu = false;
                    } else if (dX > 0) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                    if (dX < 0 && !center_viewHolder.isSwipemenu) {//左滑走自定义的方法
                        Log.d("CustomRecyclerView", " 通知向左滑动 ");
                        STATIC_INSTANCE_UTILS.manimationManager.Notification_center_item_slideleft(center_viewHolder.notification_center_item, center_viewHolder.notification_center_item_settings, -center_viewHolder.notification_center_item_settings.getWidth() - 10 * StaticVariableUtils.widthPixels / 1920);
                        center_viewHolder.isSwipemenu = true;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.d("CustomRecyclerView调用顺序", " onSelectedChanged ");
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    //滑动距离超过这个阈值，将Item全部移除到显示画面之外，左移dX最后停在负的RecyclerView宽度，右移dX最后停在正的RecyclerView宽度，之后触发onSwiped
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return .7f;//0.5f为Item宽度一半
    }

    @Override
    //滑动速度超过这个阈值，将Item全部移除到显示画面之外，左移dX最后停在负的RecyclerView宽度，右移dX最后停在正的RecyclerView宽度，之后触发onSwiped
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Float.MAX_VALUE;
    }

}
