package com.color.osd.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;

public class Menu_comments {

    Context mycontext;

    static ComponentName cn;
    static final String PKG_MARK = "com.mphotool.mark";

    private Intent intent;

    public Menu_comments(Context context) {
        mycontext = context;
    }

    public void setOnclick(View menu_comments) {
        menu_comments.setOnClickListener(v -> {
            //添加信源切换界面
            intent = new Intent();
            if (cn == null) {
                cn = new ComponentName(PKG_MARK, PKG_MARK + ".MarkService");
            }
            intent.setComponent(cn);
            DialogMenu.mydialog.dismiss();//打开批注收起菜单栏
            MenuService.menuOn = false;
            mycontext.startService(intent);

        });
    }
}
