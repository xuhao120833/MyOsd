package com.color.systemui.interfaces;

import com.color.systemui.utils.InstanceUtils;

public interface Instance<T> {

    InstanceUtils STATIC_INSTANCE_UTILS = new InstanceUtils();


    default T getInstance(T object) {

        return STATIC_INSTANCE_UTILS.getInstance(object) != null ? (T) STATIC_INSTANCE_UTILS.getInstance(object) : null;

    }

    default void setInstance(T object) {

        //Log.d("Instance<T>","setInstance");
        STATIC_INSTANCE_UTILS.setInstanc(object);

    }


}
