package com.color.osd;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements KeyEvent.Callback {

    //private boolean menu_open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d("MainActivity ", "启动");


//        Intent myintent = new Intent(MainActivity.this, MenuService.class);
//        myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(myintent)
//        moveTaskToBack(isFinishing());
    }


//keyevent接收，方法2：
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) { //只有activity在前台的时候可以收到
//        // 处理按键按下事件
//        Toast.makeText(MainActivity.this, "收到keyevent", Toast.LENGTH_SHORT).show();
//        return true;
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        // 处理按键抬起事件
//        return true;
//    }


//keyevent接收，方法1：
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {//activity 在前台时可以收到
//
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_MENU:
//                Toast.makeText(MainActivity.this, "收到菜单按钮了", Toast.LENGTH_SHORT).show();
//                menu_open = true;
//                break;
//
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

}
