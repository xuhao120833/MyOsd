package com.color.osd.models;

import android.content.Context;
import android.view.View;

import com.color.osd.ui.Source_View;

public class Menu_source {

    Context mycontext;

    Source_View source_view;

    public static boolean sourceon = false;
    Menu_source (Context context) {
        mycontext = context;

        source_view = new Source_View(mycontext);
    }


    public void setOnclick(View menu_source) {
        menu_source.setOnClickListener(v -> {
            //添加信源切换界面
            FunctionBind.mavts.addView(source_view.source , source_view.lp);
            sourceon = true;

        });
    }
}
