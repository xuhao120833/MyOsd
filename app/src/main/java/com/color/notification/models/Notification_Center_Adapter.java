package com.color.notification.models;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Notification_Center_Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Instance {

    private Context mycontext;

//    private View notification_quick_settings;
//
//    private PackageManager packageManager;
//
//    private ApplicationInfo applicationInfo;
//
//    private Drawable drawable;

    public List<Notification_Item> list = new ArrayList<>();

    private int number = 0;

    public View notification_center;

    public View notification_center_title;

    public Center_ViewHolder center_viewHolder;

    public Center_Title_ViewHolder center_title_viewHolder;

    public String TAG = "Notification_Center_Adapter";

    public Notification_Center_Adapter() {

    }

    public void setContext(Context context, List list) throws PackageManager.NameNotFoundException {
        mycontext = context;
        this.list = list;

//        packageManager = mycontext.getPackageManager();
//        applicationInfo = packageManager.getApplicationInfo("com.mphotool.whiteboard", 0);
//        drawable = applicationInfo.loadIcon(packageManager);
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("onCreateViewHolder", " " + String.valueOf(number));
//        if (number == 0) {
//            number++;
//            notification_center_title = LayoutInflater.from(mycontext).inflate(R.layout.notification_center_title, parent, false);
//            setCenterTitleClick();
//            center_title_viewHolder = new Center_Title_ViewHolder(notification_center_title);
//            return (T) center_title_viewHolder;
//        } else if (number > 0) {
//            number++;
        notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center, parent, false);
        Timer timer = new Timer();
        center_viewHolder = new Center_ViewHolder(notification_center);
        return (T) center_viewHolder;
//        }

//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, @SuppressLint("RecyclerView") int position) {
        if (holder.getClass() == Center_ViewHolder.class) {
            bindItemViewHolder((Center_ViewHolder) holder, position);

            notification_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (((Center_ViewHolder) holder).pendingIntent != null) {
                            ((Center_ViewHolder) holder).pendingIntent.send();
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemChanged(0, list.size());
                        } else {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemChanged(0, list.size());
                        }
                    } catch (PendingIntent.CanceledException e) {
                        Log.d(TAG, "pendingIntent 为空");
                        throw new RuntimeException(e);
                    }
                }
            });
        } else if (holder.getClass() == Center_Title_ViewHolder.class) {
            // 处理 Quick_Settings_ViewHolder 的逻辑
            // 例如：quick_settings_viewHolder.appName.setText(...)
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull T holder) {//避免内存泄漏的情况
        super.onViewRecycled(holder);

//        if (holder.getClass() == Center_ViewHolder.class) {
//            ((Center_ViewHolder) holder).cancelTimer();
//        }
    }

    class Center_ViewHolder extends RecyclerView.ViewHolder {

        private Handler handler = new Handler(Looper.getMainLooper());

        TextView appName, time, content;

        ImageView Icon;

        PendingIntent pendingIntent;

        int position;

        public Center_ViewHolder(View view) {
            super(view);
            //可以通过findViewById方法获取布局中的TextView
            appName = (TextView) view.findViewById(R.id.appName);

            time = (TextView) view.findViewById(R.id.time);

            content = (TextView) view.findViewById(R.id.content);

            Icon = (ImageView) view.findViewById(R.id.Icon);

            //定时任务

            // 在定时器中使用 Handler 来更新 UI
            startTimer(1);
            startTimer(2);
            startTimer(3);
            startTimer(4);
            startTimer(5);
            startTimer(10);
            startTimer(15);
            startTimer(20);
            startTimer(25);
            startTimer(30);
            startTimer(35);
            startTimer(40);
            startTimer(45);
            startTimer(50);
            startTimer(55);
            startTimer(60);
            startTimer(120);
            startTimer(150);
            startTimer(180);
            startTimer(210);
            startTimer(240);
            startTimer(270);
            startTimer(300);
        }

        private void startTimer(final int minutes) {
            long delayMillis = minutes * 60 * 1000;  // 将分钟转换为毫秒
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 在主线程上更新 UI
                            switch (minutes) {
                                case 1:
                                    time.setText("1分钟前");
                                    break;
                                case 2:
                                    time.setText("2分钟前");
                                    break;
                                case 3:
                                    time.setText("3分钟前");
                                    break;
                                case 4:
                                    time.setText("4分钟前");
                                    break;
                                case 5:
                                    time.setText("5分钟前");
                                    break;
                                case 10:
                                    time.setText("10分钟前");
                                    break;
                                case 15:
                                    time.setText("15分钟前");
                                    break;
                                case 20:
                                    time.setText("20分钟前");
                                    break;
                                case 25:
                                    time.setText("25分钟前");
                                    break;
                                case 30:
                                    time.setText("30分钟前");
                                    break;
                                case 35:
                                    time.setText("35分钟前");
                                    break;
                                case 40:
                                    time.setText("40分钟前");
                                    break;
                                case 45:
                                    time.setText("45分钟前");
                                    break;
                                case 50:
                                    time.setText("50分钟前");
                                    break;
                                case 55:
                                    time.setText("55分钟前");
                                    break;
                                case 60:
                                    time.setText("1小时前");
                                    break;
                                case 90:
                                    time.setText("1.5小时前");
                                    break;
                                case 120:
                                    time.setText("2小时前");
                                    break;
                                case 150:
                                    time.setText("2.5小时前");
                                    break;
                                case 180:
                                    time.setText("3小时前");
                                    break;
                                case 210:
                                    time.setText("3.5小时前");
                                    break;
                                case 240:
                                    time.setText("4小时前");
                                    break;
                                case 270:
                                    time.setText("4.5小时前");
                                    break;
                                case 300:
                                    time.setText("5小时前");
                                    break;
                                // 添加其他时间间隔的文本设置...
                            }
                        }
                    });
                }
            }, delayMillis);

            timer = null;


        }

//        // 在 ViewHolder 被移除时调用，取消定时器
//        public void cancelTimer() {
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//        }

    }


    class Center_Title_ViewHolder extends RecyclerView.ViewHolder {

        public Center_Title_ViewHolder(View view) {
            super(view);
        }
    }


    private void bindItemViewHolder(Center_ViewHolder holder, int position) {
        if (list.size() != 0) {
            holder.appName.setText(list.get(position).appName);
            holder.time.setText(list.get(position).time);
            holder.content.setText(list.get(position).content);
            holder.Icon.setImageDrawable(list.get(position).Icon);
            holder.pendingIntent = list.get(position).pendingIntent;
            holder.position = position;

        }
    }

    private void setCenterTitleClick() {
        View quit = notification_center_title.findViewById(R.id.quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list.clear();
//                notifyDataSetChanged();
//                STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
//                STATIC_INSTANCE_UTILS.myNotification.notification_center_linear.setVisibility(View.GONE);
//                STATIC_INSTANCE_UTILS.myNotification.up.setVisibility(View.GONE);
            }
        });
    }

}
