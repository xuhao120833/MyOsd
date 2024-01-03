package com.color.notification.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
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

public class Notification_Center_Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Instance {

    private Context mycontext;

    public View notification_center;

    public View notification_center_title;

//    private View notification_quick_settings;
//
//    private PackageManager packageManager;
//
//    private ApplicationInfo applicationInfo;
//
//    private Drawable drawable;

    private List<Notification_Item> list = new ArrayList<>();

    private int number = 0;

    public Center_ViewHolder center_viewHolder;

    public Center_Title_ViewHolder center_title_viewHolder;

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
        if (number == 0) {
            number++;
            notification_center_title = LayoutInflater.from(mycontext).inflate(R.layout.notification_center_title, parent, false);
            setCenterTitleClick();
            center_title_viewHolder = new Center_Title_ViewHolder(notification_center_title);
            return (T) center_title_viewHolder;
        } else if (number > 0) {
            number++;
            notification_center = LayoutInflater.from(mycontext).inflate(R.layout.notification_center, parent, false);
            center_viewHolder = new Center_ViewHolder(notification_center);
            return (T) center_viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        if (holder.getClass() == Center_ViewHolder.class) {
            bindItemViewHolder((Center_ViewHolder) holder, position);
        } else if (holder.getClass() == Center_Title_ViewHolder.class) {
            // 处理 Quick_Settings_ViewHolder 的逻辑
            // 例如：quick_settings_viewHolder.appName.setText(...)
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Center_ViewHolder extends RecyclerView.ViewHolder {
        TextView appName, time, content;

        ImageView Icon;

        public Center_ViewHolder(View view) {
            super(view);
            //可以通过findViewById方法获取布局中的TextView
            appName = (TextView) view.findViewById(R.id.appName);

            time = (TextView) view.findViewById(R.id.time);

            content = (TextView) view.findViewById(R.id.content);

            Icon = (ImageView) view.findViewById(R.id.Icon);
        }
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
        }
    }

    private void setCenterTitleClick() {
        View quit = notification_center_title.findViewById(R.id.quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
//                STATIC_INSTANCE_UTILS.myNotification.notification_center_linear.setVisibility(View.GONE);
//                STATIC_INSTANCE_UTILS.myNotification.up.setVisibility(View.GONE);
            }
        });
    }
}
