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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.view.CustomSeekBar;
import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public View notification_center_item, notification_center_item_content, icon, appName, time, content, Up_Or_Down;

    public View notification_center_title;

    public Center_ViewHolder center_viewHolder;

    public Center_Title_ViewHolder center_title_viewHolder;

    public String TAG = "Notification_Center_Adapter";

    private static final int VIEW_TYPE_TYPE = 1;

    private static final int VIEW_TYPE_TYPE_BlueTooth = 2;

    public Notification_Center_Adapter() {

    }

    public void setContext(Context context, List list) throws PackageManager.NameNotFoundException {
        mycontext = context;
        this.list = list;

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Notification_Center_Adapter", " 执行onCreateViewHolder");

        Log.d("Notification_Center_Adapter ", " viewType值 " + String.valueOf(viewType));

        myparent = parent;
        if (list.size() != 0 && StaticVariableUtils.bluetooth_delivery.equals("off") && viewType == 1) {
            notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center, parent, false);
            center_viewHolder = new Center_ViewHolder(notification_center);

            StaticVariableUtils.onCreate_To_onBind = true;
            return (T) center_viewHolder;
        } else if ((list.size() != 0 && StaticVariableUtils.bluetooth_delivery.equals("on")) || viewType == 2) {
            Log.d("Notification_Center_Adapter", " 创建蓝牙通知");
            notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center_lanya, parent, false);
            center_title_viewHolder = new Center_Title_ViewHolder(notification_center);
            StaticVariableUtils.onCreate_To_onBind = true;
            return (T) center_title_viewHolder;

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position Item位置
     * @return 返回viewType
     * TODO:这里返回的viewType，在onCreateViewHolder中接收，用于在通知中心有蓝牙通知时切换语言，判断改创建哪一个Holder
     */
    @Override
    public int getItemViewType(int position) {
        // 根据位置信息返回不同的 viewType

        if (list.indexOf(StaticVariableUtils.notification_item_lanya) != -1 && list.indexOf(StaticVariableUtils.notification_item_lanya) == position) {
            return VIEW_TYPE_TYPE_BlueTooth;
        } else {
            return VIEW_TYPE_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, @SuppressLint("RecyclerView") int position) {

        try {
            Log.d("Notification_Center_Adapter ", " 蓝牙position的值为 " + String.valueOf(list.indexOf(StaticVariableUtils.notification_item_lanya)));
            Log.d("Notification_Center_Adapter ", "5、 onBindViewHolder ，对应position值为 " + String.valueOf(position));

            if (holder.getClass() == Center_ViewHolder.class) {

                Log.d("Notification_Center_Adapter ", "普通通知绑定点击事件 " + String.valueOf(position));

                if (list.get(position).mynotification_center == null || list.get(position).Item_trigger_onCreate) {

                    bindItemViewHolder((Center_ViewHolder) holder, position);
                    list.get(position).mynotification_center = notification_center;

                    list.get(position).Item_trigger_onCreate = false;

                }
//            bindItemViewHolder((Center_ViewHolder) holder, position);
//            list.get(position).mynotification_center = notification_center;
            } else if (holder.getClass() == Center_Title_ViewHolder.class) {

                Log.d("Notification_Center_Adapter ", "蓝牙通知绑定点击事件 " + String.valueOf(position));

                if (list.get(position).mynotification_center == null || list.get(position).Item_trigger_onCreate) {

                    Log.d("Notification_Center_Adapter ", " 改变语言，蓝牙的位置 " + String.valueOf(position));

                    bindSeekbarHolder((Center_Title_ViewHolder) holder, position);
                    list.get(position).mynotification_center = notification_center;

                    list.get(position).Item_trigger_onCreate = false;

                }
            }

            StaticVariableUtils.onCreate_To_onBind = false;
            if (StaticVariableUtils.bluetooth_delivery.equals("on")) {
                StaticVariableUtils.bluetooth_delivery = "off";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    }

    public class Center_ViewHolder extends RecyclerView.ViewHolder {

        private Handler handler = new Handler(Looper.getMainLooper());

        TextView appName = null, time = null, content = null;

        public ImageView Icon = null;

        PendingIntent pendingIntent = null;

        Notification_Item notification_item = null;

        public int onCreat_minutes = 0, onCreat_hours = 0;

        public View mynotification_center = null;

        public View notification_center_item = null;

        public View notification_center_item_settings = null;

        //三个右滑菜单按键
        public View top = null;

        public View delete = null;

        public View settings = null;

        //菜单是否展开的标志位
        public boolean isSwipemenu = false;


        public Center_ViewHolder(View view) {
            super(view);
            //可以通过findViewById方法获取布局中的TextView
            appName = (TextView) view.findViewById(R.id.appName);

            time = (TextView) view.findViewById(R.id.time);

            content = (TextView) view.findViewById(R.id.content);

            Icon = (ImageView) view.findViewById(R.id.Icon);

            notification_center_item = view.findViewById(R.id.notification_center_item);

            notification_center_item_settings = view.findViewById(R.id.notification_center_item_settings);

            top = view.findViewById(R.id.top);

            delete = view.findViewById(R.id.delete);

            settings = view.findViewById(R.id.settings);


            if (mynotification_center == null) {
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
                                    if (onCreat_minutes == 0) {
                                        time.setText(1 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 59) {
                                            time.setText(onCreat_minutes + 1 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }

//                                    time.setText("1分钟前");
                                    break;
                                case 2:
                                    if (onCreat_minutes == 0) {
                                        time.setText(2 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 58) {
                                            time.setText(onCreat_minutes + 2 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("2分钟前");
                                    break;
                                case 3:
                                    if (onCreat_minutes == 0) {
                                        time.setText(3 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 57) {
                                            time.setText(onCreat_minutes + 3 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("3分钟前");
                                    break;
                                case 4:
                                    if (onCreat_minutes == 0) {
                                        time.setText(4 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 56) {
                                            time.setText(onCreat_minutes + 4 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("4分钟前");
                                    break;
                                case 5:
                                    if (onCreat_minutes == 0) {
                                        time.setText(5 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 55) {
                                            time.setText(onCreat_minutes + 5 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("5分钟前");
                                    break;
                                case 10:
                                    if (onCreat_minutes == 0) {
                                        time.setText(10 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 50) {
                                            time.setText(onCreat_minutes + 10 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("10分钟前");
                                    break;
                                case 15:
                                    if (onCreat_minutes == 0) {
                                        time.setText(15 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 45) {
                                            time.setText(onCreat_minutes + 15 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("15分钟前");
                                    break;
                                case 20:
                                    if (onCreat_minutes == 0) {
                                        time.setText(20 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 40) {
                                            time.setText(onCreat_minutes + 20 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("20分钟前");
                                    break;
                                case 25:
                                    if (onCreat_minutes == 0) {
                                        time.setText(25 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 35) {
                                            time.setText(onCreat_minutes + 25 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("25分钟前");
                                    break;
                                case 30:
                                    if (onCreat_minutes == 0) {
                                        time.setText(30 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 30) {
                                            time.setText(onCreat_minutes + 30 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("30分钟前");
                                    break;
                                case 35:
                                    if (onCreat_minutes == 0) {
                                        time.setText(35 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 25) {
                                            time.setText(onCreat_minutes + 35 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("35分钟前");
                                    break;
                                case 40:
                                    if (onCreat_minutes == 0) {
                                        time.setText(40 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 20) {
                                            time.setText(onCreat_minutes + 40 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("40分钟前");
                                    break;
                                case 45:
                                    if (onCreat_minutes == 0) {
                                        time.setText(45 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 15) {
                                            time.setText(onCreat_minutes + 45 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("45分钟前");
                                    break;
                                case 50:
                                    if (onCreat_minutes == 0) {
                                        time.setText(50 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 10) {
                                            time.setText(onCreat_minutes + 50 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("50分钟前");
                                    break;
                                case 55:
                                    if (onCreat_minutes == 0) {
                                        time.setText(55 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 5) {
                                            time.setText(onCreat_minutes + 55 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("55分钟前");
                                    break;
                                case 60:
                                    if (onCreat_hours == 0) {
                                        time.setText(1 + mycontext.getString(R.string.一小时前));
                                    } else {
                                        time.setText(onCreat_hours + 1 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(1 + mycontext.getString(R.string.一小时前));
//                                    time.setText("1小时前");
                                    break;
                                case 90:
                                    if (onCreat_hours == 0) {
                                        time.setText(1.5 + mycontext.getString(R.string.一小时前));
                                    } else {
                                        time.setText(onCreat_hours + 1.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(1.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("1.5小时前");
                                    break;
                                case 120:
                                    if (onCreat_hours == 0) {
                                        time.setText(2 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 2 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(2 + mycontext.getString(R.string.小时前));
//                                    time.setText("2小时前");
                                    break;
                                case 150:
                                    if (onCreat_hours == 0) {
                                        time.setText(2.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 2.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(2.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("2.5小时前");
                                    break;
                                case 180:
                                    if (onCreat_hours == 0) {
                                        time.setText(3 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 3 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(3 + mycontext.getString(R.string.小时前));
//                                    time.setText("3小时前");
                                    break;
                                case 210:
                                    if (onCreat_hours == 0) {
                                        time.setText(3.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 3.5 + mycontext.getString(R.string.小时前));
                                    }
//                                    time.setText(3.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("3.5小时前");
                                    break;
                                case 240:
                                    if (onCreat_hours == 0) {
                                        time.setText(4 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 4 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(4 + mycontext.getString(R.string.小时前));
//                                    time.setText("4小时前");
                                    break;
                                case 270:
                                    if (onCreat_hours == 0) {
                                        time.setText(4.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 4.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(4.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("4.5小时前");
                                    break;
                                case 300:
                                    if (onCreat_hours == 0) {
                                        time.setText(5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(5 + mycontext.getString(R.string.小时前));
//                                    time.setText("5小时前");
                                    break;
                                // 添加其他时间间隔的文本设置...
                            }
                        }
                    });
                }
            }, delayMillis);

            timer = null;


        }


    }


    public class Center_Title_ViewHolder extends RecyclerView.ViewHolder {

        private Handler handler = new Handler(Looper.getMainLooper());

        TextView appName = null, time = null, lanya_seekbar_text, transmission, filename;

        CustomSeekBar lanya_seekbar = null;

        ImageView Icon = null;

        ImageView Icon2 = null;

        PendingIntent pendingIntent = null;

        Notification_Item notification_item = null;

        int onCreat_minutes = 0, onCreat_hours = 0;

        public View mynotification_center = null;

        public Center_Title_ViewHolder(View view) {
            super(view);

            appName = (TextView) view.findViewById(R.id.appName_lanya);

            time = (TextView) view.findViewById(R.id.time_lanya);

            lanya_seekbar = (CustomSeekBar) view.findViewById(R.id.seekbar_lanya);
            //禁止SeekBar被手动拖动
            lanya_seekbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //直接消费掉触摸事件
                    return true;
                }
            });

            lanya_seekbar_text = (TextView) view.findViewById(R.id.seekbar_lanya_text);

            Icon = (ImageView) view.findViewById(R.id.Icon_lanya);

            Icon2 = (ImageView) view.findViewById(R.id.Icon_lanya2);

            transmission = view.findViewById(R.id.transmission);

            filename = view.findViewById(R.id.filename);

            if (mynotification_center == null) {
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
                                    if (onCreat_minutes == 0) {
                                        time.setText(1 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 59) {
                                            time.setText(onCreat_minutes + 1 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }

//                                    time.setText("1分钟前");
                                    break;
                                case 2:
                                    if (onCreat_minutes == 0) {
                                        time.setText(2 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 58) {
                                            time.setText(onCreat_minutes + 2 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("2分钟前");
                                    break;
                                case 3:
                                    if (onCreat_minutes == 0) {
                                        time.setText(3 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 57) {
                                            time.setText(onCreat_minutes + 3 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("3分钟前");
                                    break;
                                case 4:
                                    if (onCreat_minutes == 0) {
                                        time.setText(4 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 56) {
                                            time.setText(onCreat_minutes + 4 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("4分钟前");
                                    break;
                                case 5:
                                    if (onCreat_minutes == 0) {
                                        time.setText(5 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 55) {
                                            time.setText(onCreat_minutes + 5 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("5分钟前");
                                    break;
                                case 10:
                                    if (onCreat_minutes == 0) {
                                        time.setText(10 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 50) {
                                            time.setText(onCreat_minutes + 10 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("10分钟前");
                                    break;
                                case 15:
                                    if (onCreat_minutes == 0) {
                                        time.setText(15 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 45) {
                                            time.setText(onCreat_minutes + 15 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("15分钟前");
                                    break;
                                case 20:
                                    if (onCreat_minutes == 0) {
                                        time.setText(20 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 40) {
                                            time.setText(onCreat_minutes + 20 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("20分钟前");
                                    break;
                                case 25:
                                    if (onCreat_minutes == 0) {
                                        time.setText(25 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 35) {
                                            time.setText(onCreat_minutes + 25 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("25分钟前");
                                    break;
                                case 30:
                                    if (onCreat_minutes == 0) {
                                        time.setText(30 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 30) {
                                            time.setText(onCreat_minutes + 30 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("30分钟前");
                                    break;
                                case 35:
                                    if (onCreat_minutes == 0) {
                                        time.setText(35 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 25) {
                                            time.setText(onCreat_minutes + 35 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("35分钟前");
                                    break;
                                case 40:
                                    if (onCreat_minutes == 0) {
                                        time.setText(40 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 20) {
                                            time.setText(onCreat_minutes + 40 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("40分钟前");
                                    break;
                                case 45:
                                    if (onCreat_minutes == 0) {
                                        time.setText(45 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 15) {
                                            time.setText(onCreat_minutes + 45 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("45分钟前");
                                    break;
                                case 50:
                                    if (onCreat_minutes == 0) {
                                        time.setText(50 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 10) {
                                            time.setText(onCreat_minutes + 50 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("50分钟前");
                                    break;
                                case 55:
                                    if (onCreat_minutes == 0) {
                                        time.setText(55 + mycontext.getString(R.string.一分钟前));
                                    } else {
                                        if (getNumberFromString(time.getText().toString()) < 5) {
                                            time.setText(onCreat_minutes + 55 + mycontext.getString(R.string.分钟前));
                                        } else {
                                            time.setText(1 + mycontext.getString(R.string.一小时前));
                                        }
                                    }
//                                    time.setText("55分钟前");
                                    break;
                                case 60:
                                    if (onCreat_hours == 0) {
                                        time.setText(1 + mycontext.getString(R.string.一小时前));
                                    } else {
                                        time.setText(onCreat_hours + 1 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(1 + mycontext.getString(R.string.一小时前));
//                                    time.setText("1小时前");
                                    break;
                                case 90:
                                    if (onCreat_hours == 0) {
                                        time.setText(1.5 + mycontext.getString(R.string.一小时前));
                                    } else {
                                        time.setText(onCreat_hours + 1.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(1.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("1.5小时前");
                                    break;
                                case 120:
                                    if (onCreat_hours == 0) {
                                        time.setText(2 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 2 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(2 + mycontext.getString(R.string.小时前));
//                                    time.setText("2小时前");
                                    break;
                                case 150:
                                    if (onCreat_hours == 0) {
                                        time.setText(2.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 2.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(2.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("2.5小时前");
                                    break;
                                case 180:
                                    if (onCreat_hours == 0) {
                                        time.setText(3 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 3 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(3 + mycontext.getString(R.string.小时前));
//                                    time.setText("3小时前");
                                    break;
                                case 210:
                                    if (onCreat_hours == 0) {
                                        time.setText(3.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 3.5 + mycontext.getString(R.string.小时前));
                                    }
//                                    time.setText(3.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("3.5小时前");
                                    break;
                                case 240:
                                    if (onCreat_hours == 0) {
                                        time.setText(4 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 4 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(4 + mycontext.getString(R.string.小时前));
//                                    time.setText("4小时前");
                                    break;
                                case 270:
                                    if (onCreat_hours == 0) {
                                        time.setText(4.5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 4.5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(4.5 + mycontext.getString(R.string.小时前));
//                                    time.setText("4.5小时前");
                                    break;
                                case 300:
                                    if (onCreat_hours == 0) {
                                        time.setText(5 + mycontext.getString(R.string.小时前));
                                    } else {
                                        time.setText(onCreat_hours + 5 + mycontext.getString(R.string.小时前));
                                    }

//                                    time.setText(5 + mycontext.getString(R.string.小时前));
//                                    time.setText("5小时前");
                                    break;
                                // 添加其他时间间隔的文本设置...
                            }
                        }
                    });
                }
            }, delayMillis);

            timer = null;

        }


    }


    private void bindItemViewHolder(Center_ViewHolder holder, int position) {
        if (list.size() != 0) {
            holder.appName.setText(list.get(position).appName);
            if (list.get(position).Item_trigger_onCreate) {
                TextView text = list.get(position).mynotification_center.findViewById(R.id.time);
                String text_string = (String) text.getText();
                Log.d("bindItemViewHolder ", text_string);
                if (text_string.contains("分钟") || text_string.contains("分鐘") || text_string.contains("minute")) {
                    Log.d("bindItemViewHolder", " text " + text.getText());
                    // 使用正则表达式提取数字
                    holder.onCreat_minutes = getNumberFromString(text_string);
                    if (holder.onCreat_minutes == 1) {
                        holder.time.setText(holder.onCreat_minutes + mycontext.getString(R.string.一分钟前));
                    } else {
                        holder.time.setText(holder.onCreat_minutes + mycontext.getString(R.string.分钟前));
                    }

                } else if (text_string.contains("小時") || text_string.contains("小时") || text_string.contains("hour")) {
                    Log.d("bindItemViewHolder", " text " + text.getText());
                    holder.onCreat_hours = getNumberFromString(text_string);
                    if (holder.onCreat_hours == 1) {
                        holder.time.setText(holder.onCreat_hours + mycontext.getString(R.string.一小时前));
                    } else {
                        holder.time.setText(holder.onCreat_hours + mycontext.getString(R.string.小时前));
                    }

                } else if (text_string.contains("现在") || text_string.contains("現在") || text_string.contains("now")) {
                    Log.d("bindItemViewHolder", " text " + text.getText());
                    holder.time.setText(mycontext.getString(R.string.现在));
                }

                if (list.get(position).isMultiple_Messages && !list.get(position).isExpand) {
                    ImageView imageView = (ImageView) notification_center.findViewById(R.id.Up_Or_Down);
                    imageView.setImageResource(R.drawable.notification_center_down);
                    if (imageView.getVisibility() != View.VISIBLE) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    holder.content.setText(list.get(position).number + 1 + mycontext.getString(R.string.个通知));
                    Log.d("bindItemViewHolder", " number值 " + list.get(position).number);
                } else if (list.get(position).isMultiple_Messages && list.get(position).isExpand) {
                    ImageView imageView = (ImageView) notification_center.findViewById(R.id.Up_Or_Down);
                    imageView.setImageResource(R.drawable.notification_center_up);
                    if (imageView.getVisibility() != View.VISIBLE) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    holder.content.setText(list.get(position).number + 1 + mycontext.getString(R.string.个通知));
                } else if (!list.get(position).isMultiple_Messages) {
                    holder.content.setText(list.get(position).content);
                }


            } else if (!list.get(position).Item_trigger_onCreate) {
                holder.time.setText(list.get(position).time);
                holder.content.setText(list.get(position).content);
            }

//            holder.content.setText(list.get(position).content);
            holder.Icon.setImageDrawable(list.get(position).Icon);
            holder.pendingIntent = list.get(position).pendingIntent;
            holder.notification_item = list.get(position);

            Log.d("notification_xu_su ", " 绑定点击事件w");
            holder.mynotification_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int mypostion = list.indexOf(holder.notification_item);
                        Log.d("notification_xu_su ", "bindItemViewHolder mypostion的值 " + String.valueOf(mypostion));
                        if (list.get(mypostion).isMultiple_Messages && !list.get(mypostion).isExpand) {
                            ImageView imageView = (ImageView) list.get(mypostion).mynotification_center.findViewById(R.id.Up_Or_Down);
                            imageView.setImageResource(R.drawable.notification_center_up);
                            //通知展开操作
                            unfold(holder);
                            mypostion = list.indexOf(holder.notification_item);
                            list.get(mypostion).isExpand = true;
                            Log.d(TAG, " 可折叠");
                        } else if (list.get(mypostion).isMultiple_Messages && list.get(mypostion).isExpand) {
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
                            //清除之前不移除子View的焦点会造成报错java.lang.IllegalArgumentException: parameter must be a descendant of this view
                            list.get(mypostion).mynotification_center.clearFocus();
                            list.remove(mypostion);
                            notifyItemRemoved(mypostion);
                            //notifyItemChanged会去执行一次onBindViewHolder
                            notifyItemChanged(0, list.size());

                            judgeParent(holder, mypostion);

                            if (StaticVariableUtils.list.size() == 0) {
                                STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                            }


                        } else if (holder.pendingIntent == null && !list.get(mypostion).isMultiple_Messages) {
                            list.get(mypostion).mynotification_center.clearFocus();
                            list.remove(mypostion);
                            //notifyItemChanged会去执行一次onBindViewHolder
                            notifyItemRemoved(mypostion);
                            notifyItemChanged(0, list.size());

                            judgeParent(holder, mypostion);

                            Log.d(TAG, " 点击，移除通知");

                            if (StaticVariableUtils.list.size() == 0) {
                                STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "pendingIntent 打不开");
                    }
                }
            });

            holder.top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(mycontext, mycontext.getString(R.string.通知置顶), Toast.LENGTH_SHORT).show();
                        //分成两类：1、普通单条通知。 2、同一个APP多条可折叠 打开的复合通知。
                        int mypostion = list.indexOf(holder.notification_item);
                        notifyItemMoved(mypostion, list.size()-1);
                        Notification_Item notification_item = list.get(mypostion);
                        list.remove(mypostion);
                        list.add(notification_item);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int mypostion = list.indexOf(holder.notification_item);
                        Log.d(TAG, " 点击事件触发");
                        if (!list.get(mypostion).isMultiple_Messages || !list.get(mypostion).isExpand) {
                            list.get(mypostion).mynotification_center.clearFocus();
                            list.remove(mypostion);
                            //notifyItemChanged会去执行一次onBindViewHolder
                            notifyItemRemoved(mypostion);
                            notifyItemChanged(0, list.size());

                            judgeParent(holder, mypostion);

                            Log.d(TAG, " 点击，移除通知");

                            if (StaticVariableUtils.list.size() == 0) {
                                STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "pendingIntent 打不开");
                    }
                }
            });

            holder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开应用通知设置界面

                }
            });

        }
    }

    private void bindSeekbarHolder(Center_Title_ViewHolder holder, int position) {
        if (list.size() != 0) {
            if (list.get(position).Item_trigger_onCreate) {

                list.get(position).appName = mycontext.getString(R.string.蓝牙);

                TextView transmission = list.get(position).mynotification_center.findViewById(R.id.transmission);
                String text_transmission = (String) transmission.getText();
                holder.transmission.setVisibility(View.VISIBLE);
                holder.transmission.setText(text_transmission);

                TextView filename = list.get(position).mynotification_center.findViewById(R.id.filename);
                String text_filename = (String) filename.getText();
                holder.filename.setText(text_filename);

                TextView appName = list.get(position).mynotification_center.findViewById(R.id.appName_lanya);
                String text_appName = (String) appName.getText();
                holder.appName.setText(text_appName);

                TextView time = list.get(position).mynotification_center.findViewById(R.id.time_lanya);
                String text_time = (String) time.getText();

                TextView lanya_progress = list.get(position).mynotification_center.findViewById(R.id.seekbar_lanya_text);
                String text_progress = (String) lanya_progress.getText();
                holder.lanya_seekbar_text.setText(text_progress);

                CustomSeekBar lanya_seekbar = list.get(position).mynotification_center.findViewById(R.id.seekbar_lanya);
                holder.lanya_seekbar.setProgress(lanya_seekbar.getProgress());

                if (text_time.contains("分钟") || text_time.contains("分鐘") || text_time.contains("minute")) {
                    holder.onCreat_minutes = getNumberFromString(text_time);
                    if (holder.onCreat_minutes == 1) {
                        holder.time.setText(holder.onCreat_minutes + mycontext.getString(R.string.一分钟前));
                    } else {
                        holder.time.setText(holder.onCreat_minutes + mycontext.getString(R.string.分钟前));
                    }
                } else if (text_time.contains("小時") || text_time.contains("小时") || text_time.contains("hour")) {
                    holder.onCreat_hours = getNumberFromString(text_time);
                    if (holder.onCreat_hours == 1) {
                        holder.time.setText(holder.onCreat_hours + mycontext.getString(R.string.一小时前));
                    } else {
                        holder.time.setText(holder.onCreat_hours + mycontext.getString(R.string.小时前));
                    }
                } else if (text_time.contains("现在") || text_time.contains("現在") || text_time.contains("now")) {
                    holder.time.setText(mycontext.getString(R.string.现在));
                }

            } else if (!list.get(position).Item_trigger_onCreate) {
                holder.appName.setText(list.get(position).appName);
                holder.time.setText(list.get(position).time);
                holder.lanya_seekbar.setProgress(list.get(position).lanya_progress);
            }
            holder.Icon.setImageDrawable(list.get(position).Icon);
            holder.pendingIntent = list.get(position).pendingIntent;
            holder.notification_item = list.get(position);

            holder.Icon2.setImageDrawable(list.get(position).Icon);

            holder.mynotification_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int mypostion = list.indexOf(holder.notification_item);

                        if (list.get(mypostion).pendingIntent != null) {
                            list.get(mypostion).pendingIntent.send();
                            list.get(mypostion).mynotification_center.clearFocus();
                            list.remove(mypostion);
                            notifyItemRemoved(mypostion);

                            //归位所有需要归位的值
                            StaticVariableUtils.lanya_first_accept_android_text = true;
                            StaticVariableUtils.lanya_number = -1;
                            StaticVariableUtils.android_lanya_progress = -1;
                            StaticVariableUtils.lanya_first_transmit = false;
                            StaticVariableUtils.notification_has_lanya = false;

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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
        int subpostion = 0;//在展开的通知中的位置
        for (String string : list.get(list.indexOf(center_viewHolder.notification_item)).multiple_content) {
            //mypostion后面添加multiple_content个Item
            Log.d(TAG, " unfold展开 " + String.valueOf(mypostion));
            Notification_Item notification_item = new Notification_Item();
            notification_item.appName = list.get(mypostion).appName;
            notification_item.Icon = list.get(mypostion).Icon;
            notification_item.subpostion = subpostion;
            notification_item.content = string;
            notification_item.pendingIntent = list.get(list.indexOf(center_viewHolder.notification_item)).multiple_Intent.get(subpostion);
            notification_item.parent_ViewHolder = center_viewHolder;
            notification_item.parent_notification_item = center_viewHolder.notification_item;
            StaticVariableUtils.recyclerView.getRecycledViewPool().clear();
            list.add(mypostion, notification_item);
            notifyItemInserted(mypostion);
            subpostion++;

        }
    }

    //同个APP，多条通知展开后的收起操作
    private void collapse(Center_ViewHolder center_viewHolder) {//同个APP，多条通知收起

        for (String string : list.get(list.indexOf(center_viewHolder.notification_item)).multiple_content) {
            //mypostion后面收起 multiple_content个Item
            int mypostion = list.indexOf(center_viewHolder.notification_item);


            list.remove(mypostion - 1);
            notifyItemRemoved(mypostion - 1);

            notifyItemChanged(0, list.size());

//            StaticVariableUtils.recyclerView.getRecycledViewPool().clear();

        }
    }

    public void judgeParent(Center_ViewHolder holder, int mypostion) {

        if (holder.notification_item.parent_notification_item == null) {
            return;
        }

        Log.d("judgeParent", " 父通知的content" + String.valueOf(holder.notification_item.parent_notification_item.content));
        holder.notification_item.parent_notification_item.number--;
        if (holder.notification_item.parent_notification_item.number >= 0) {
            holder.notification_item.parent_notification_item.multiple_content.remove(holder.notification_item.parent_notification_item.number);
            holder.notification_item.parent_notification_item.multiple_Intent.remove(holder.notification_item.parent_notification_item.number);
            holder.notification_item.parent_ViewHolder.content.setText(holder.notification_item.parent_notification_item.number + 1 + mycontext.getString(R.string.个通知));

        } else if (holder.notification_item.parent_notification_item.number < 0) {

            mypostion = list.indexOf(holder.notification_item.parent_notification_item);
            list.remove(mypostion);
            notifyItemRemoved(mypostion);
            notifyItemChanged(0, list.size());
        }

    }

    private int getNumberFromString(String text_string) {
        Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
        Matcher matcher = pattern.matcher(text_string);
        String extractedNumber = "";
        // 查找匹配的数字
        if (matcher.find()) {
            extractedNumber = matcher.group();
            Log.d("bindItemViewHolder", " 提取到的数字 " + extractedNumber);
            return Integer.parseInt(extractedNumber);
        } else {
            return 0;
        }


    }

    public void onItemLeftSwiped(int position) {
        // 左滑操作的处理逻辑
    }

    public void onItemRightSwiped(int position) {
        // 右滑操作的处理逻辑
    }

}
