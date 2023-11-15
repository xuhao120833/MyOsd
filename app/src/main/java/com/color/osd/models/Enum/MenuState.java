package com.color.osd.models.Enum;

/**
 * 用于记录菜单的状态
 */
public enum MenuState {
    MENU_BRIGHTNESS,   // 亮度
    MENU_COMMENTS,
    MENU_EYE,
    MENU_SCREENSHOT,
    MENU_VOLUME,       // 声音（OSD面板中，点击“声音按钮”呼出的状态，正统的）
    MENU_VOLUME_DIRECT, // 声音直接呼出态（遥控器上的音量+-直接呼出的状态，非正统的）
    MENU_SOURCE,
    NULL,
    MENU_BRIGHTNESS_VOLUME,   // 声音和亮度的复合态
    MENU_BRIGHTNESS_FOCUS,   // 声音和亮度的复合态的时候，亮度被聚焦
    MENU_VOLUME_FOCUS   // 声音和亮度的复合态的时候，声音被聚焦
}
