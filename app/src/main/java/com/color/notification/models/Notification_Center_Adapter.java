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
import com.color.systemui.utils.StaticVariableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @创建者 kevinxu
 * @创建时间 2023/12/28 10:11
 * @类描述 {TODO}消息中心适配器
 */
public class Notification_Center_Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Instance {

    private Context mycontext;

//    private View notification_quick_settings;
//
//    private PackageManager packageManager;
//
//    private ApplicationInfo applicationInfo;
//
//    private Drawable drawable;

    //定义变量接收接口
    private OnItemClickListener mOnItemClickListener;

    public List<Notification_Item> list = new ArrayList<>();

    private int number = 0;

    private LayoutInflater inflater;

    private ViewGroup myparent;

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

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        packageManager = mycontext.getPackageManager();
//        applicationInfo = packageManager.getApplicationInfo("com.mphotool.whiteboard", 0);
//        drawable = applicationInfo.loadIcon(packageManager);
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myparent = parent;
        Log.d("MyNotificationService", " 走到onCreateViewHolder");

        Log.d("notification_xu_su ", "4、 onCreateViewHolder " + String.valueOf(viewType));
//        if (number == 0) {
//            number++;
//            notification_center_title = LayoutInflater.from(mycontext).inflate(R.layout.notification_center_title, parent, false);
//            setCenterTitleClick();
//            center_title_viewHolder = new Center_Title_ViewHolder(notification_center_title);
//            return (T) center_title_viewHolder;
//        } else if (number > 0) {
//            number++;
        if(list.size() != 0) {
//            inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            leftNavibar = inflater.inflate(R.layout.navibar_left, null);
//            notification_center = inflater.inflate(R.layout.notification_center, null);
            notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center, myparent, false);
            center_viewHolder = new Center_ViewHolder(notification_center);
            StaticVariableUtils.onCreate_To_onBind = true;
            return (T) center_viewHolder;
        }
        return null;
//        }

//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, @SuppressLint("RecyclerView") int position) {

        if(!StaticVariableUtils.onCreate_To_onBind) {
//            notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center, myparent, false);
//            center_viewHolder = new Center_ViewHolder(notification_center);
//            holder = (T) center_viewHolder;

        }

        if (holder.getClass() == Center_ViewHolder.class) {


            Log.d("notification_xu_su ", "5、 onBindViewHolder ，对应position值为 "+String.valueOf(position));
//            Log.d("notification_xu_su ", "5、 onBindViewHolder ，对应list中的APP是 "+String.valueOf(list.get(position).appName));
//            Log.d("notification_xu_su ", "5、 onBindViewHolder ，对应list中的content是 "+String.valueOf(list.get(position).content));
//            Log.d("notification_xu_su ", "5、 onBindViewHolder ，现在holder中的content是 "+String.valueOf(((Center_ViewHolder) holder).content));
//            Log.d("notification_xu_su ", "5、 onBindViewHolder ，现在holder中的APP是 "+String.valueOf(((Center_ViewHolder) holder).appName));
//            bindItemViewHolder((Center_ViewHolder) holder, position);

            if(list.get(position).mynotification_center == null) {
                Log.d("notification_xu_su ", "5、 绑定点击事件");
                bindItemViewHolder((Center_ViewHolder) holder, position);
                list.get(position).mynotification_center = notification_center;
            }
//            bindItemViewHolder((Center_ViewHolder) holder, position);
//            list.get(position).mynotification_center = notification_center;
        } else if (holder.getClass() == Center_Title_ViewHolder.class) {
            // 处理 Quick_Settings_ViewHolder 的逻辑
            // 例如：quick_settings_viewHolder.appName.setText(...)
        }

        StaticVariableUtils.onCreate_To_onBind = false;

    }

    @Override
    public int getItemCount() {

//        Log.d("notification_xu_su ", "6、 getItemCount");
//
//        Log.d("MyNotificationService"," 走到getItemCount，列表的大小"+String.valueOf(list.size()));\
        Log.d("notification_xu_su ", "3、 list 大小 " + String.valueOf(list.size()));
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

        TextView appName = null, time = null, content = null;

        ImageView Icon =null;

        PendingIntent pendingIntent = null;

        Notification_Item notification_item = null;


        public View mynotification_center = null;

        public Center_ViewHolder(View view) {
            super(view);
            //可以通过findViewById方法获取布局中的TextView
            appName = (TextView) view.findViewById(R.id.appName);

            time = (TextView) view.findViewById(R.id.time);

            content = (TextView) view.findViewById(R.id.content);

            Icon = (ImageView) view.findViewById(R.id.Icon);

            if(mynotification_center == null) {
                mynotification_center = view;
            }

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
            holder.notification_item = list.get(position);

            holder.mynotification_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int mypostion = list.indexOf(holder.notification_item);
                        Log.d("notification_xu_su ", "bindItemViewHolder mypostion的值 "+String.valueOf(mypostion));
                        if (list.get(mypostion).isMultiple_Messages && !list.get(mypostion).isExpand) {
                            ImageView imageView = (ImageView) list.get(mypostion).mynotification_center.findViewById(R.id.Up_Or_Down);
                            imageView.setImageResource(R.drawable.notification_center_up);
                            //通知展开操作
                            unfold(holder);
                            mypostion = list.indexOf(holder.notification_item);
                            list.get(mypostion).isExpand = true;
                            Log.d(TAG, " 可折叠");
                        } else if(list.get(mypostion).isMultiple_Messages && list.get(mypostion).isExpand) {
                            ImageView imageView = (ImageView) list.get(mypostion).mynotification_center.findViewById(R.id.Up_Or_Down);
                            imageView.setImageResource(R.drawable.notification_center_down);
                            //通知的收起操作
                            collapse(holder);
                            mypostion = list.indexOf(holder.notification_item);
                            list.get(mypostion).isExpand = false;
                            Log.d(TAG, " 可打开");
                        }
                        Log.d(TAG, " 点击事件触发");
                        if (holder.pendingIntent != null && !list.get(mypostion).isMultiple_Messages) {
                            Log.d(TAG, " 点击打开URL，移除通知");
                            holder.pendingIntent.send();
                            list.remove(mypostion);
                            notifyItemRemoved(mypostion);

                            //会去执行一次onBindViewHolder
                            notifyItemChanged(0, list.size());

                        } else if(holder.pendingIntent == null && !list.get(mypostion).isMultiple_Messages) {
                            list.remove(mypostion);
                            notifyItemRemoved(mypostion);

                            notifyItemChanged(0, list.size());
                            Log.d(TAG, " 点击，移除通知");

                        }
                    } catch (PendingIntent.CanceledException e) {
                        Log.d(TAG, "pendingIntent 打不开");
                    }
                }
            });

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

    //定义接口：点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击

        void onItemLongClick(View view, int position);//长按
    }

    //设置接口接收的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    private void unfold(Center_ViewHolder center_viewHolder) {

        int mypostion = list.indexOf(center_viewHolder.notification_item);
        for(String string : list.get(list.indexOf(center_viewHolder.notification_item)).multiple_content) {
            //mypostion后面添加multiple_content个Item
            Log.d(TAG," unfold展开 "+String.valueOf(mypostion));
            Notification_Item notification_item = new Notification_Item();
            notification_item.appName = list.get(mypostion).appName;
            notification_item.Icon = list.get(mypostion).Icon;
            notification_item.content = string;
            notification_item.parent_ViewHolder = center_viewHolder;
            StaticVariableUtils.recyclerView.getRecycledViewPool().clear();
            list.add(mypostion ,notification_item);
            notifyItemInserted(mypostion);

        }
    }

    //同个APP，多条通知展开后的收起操作
    private void collapse(Center_ViewHolder center_viewHolder) {//同个APP，多条通知展开

        for(String string : list.get(list.indexOf(center_viewHolder.notification_item)).multiple_content) {
            //mypostion后面收起 multiple_content个Item
            int mypostion = list.indexOf(center_viewHolder.notification_item);


            list.remove(mypostion-1);
            notifyItemRemoved(mypostion-1);

            notifyItemChanged(0, list.size());

//            StaticVariableUtils.recyclerView.getRecycledViewPool().clear();

        }
    }

}
