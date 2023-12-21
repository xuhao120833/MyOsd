package com.color.notification.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Instance {

    private Context mycontext;

    private View notification_item;

    private PackageManager packageManager;

    private ApplicationInfo applicationInfo;

    private Drawable drawable;

    private List<Notifi> list = new ArrayList<>();

    public MyViewHolder myViewHolder;

    public RecycleViewAdapter() {

    }

    public void setContext(Context context, List list) throws PackageManager.NameNotFoundException {
        mycontext = context;
        this.list = list;

        packageManager = mycontext.getPackageManager();
        applicationInfo = packageManager.getApplicationInfo("com.mphotool.whiteboard", 0);
        drawable = applicationInfo.loadIcon(packageManager);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        notification_item = LayoutInflater.from(mycontext).inflate(R.layout.notification_item, parent, false);
        myViewHolder = new MyViewHolder(notification_item);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.MyViewHolder holder, int position) {
        if (list.size() != 0) {
            holder.appName.setText(list.get(position).appName);
            holder.time.setText(list.get(position).time);
            holder.content.setText(list.get(position).content);
            holder.Icon.setImageDrawable(list.get(position).Icon);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView appName, time, content;

        ImageView Icon;

        public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
            //可以通过findViewById方法获取布局中的TextView
            appName = (TextView) itemView.findViewById(R.id.appName);

            time = (TextView) itemView.findViewById(R.id.time);

            content = (TextView) itemView.findViewById(R.id.content);

            Icon = (ImageView) itemView.findViewById(R.id.Icon);
        }
    }
}
