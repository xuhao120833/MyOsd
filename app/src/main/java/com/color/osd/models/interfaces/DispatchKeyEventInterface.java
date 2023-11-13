package com.color.osd.models.interfaces;

import android.view.KeyEvent;

import com.color.osd.models.Enum.MenuState;

public interface DispatchKeyEventInterface {
    boolean onKeyEvent(KeyEvent event, MenuState menuState);
    boolean isHomeKeyEvent(KeyEvent event);
    boolean isBackKeyEvent(KeyEvent event);
}
