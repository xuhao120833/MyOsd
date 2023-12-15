package com.color.systemui.interfaces;

import android.util.Log;

import com.color.systemui.utils.StaticInstanceUtils;

public interface Instance<T> {

    StaticInstanceUtils STATIC_INSTANCE_UTILS = new StaticInstanceUtils();


    default T getInstance(T object) {

        return STATIC_INSTANCE_UTILS.getInstance(object) != null ? (T) STATIC_INSTANCE_UTILS.getInstance(object) : null;

    }

    default void setInstance(T object) {

        Log.d("Instance<T>","setInstance");
        STATIC_INSTANCE_UTILS.setInstanc(object);

    }


}
