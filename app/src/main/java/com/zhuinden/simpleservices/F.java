package com.zhuinden.simpleservices;

import com.google.auto.value.AutoValue;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class F
        extends Key {
    @Override
    public int layout() {
        return R.layout.path_f;
    }

    public static F create() {
        return new AutoValue_F();
    }
}
