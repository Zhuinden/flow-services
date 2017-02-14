package com.zhuinden.simpleservices;

import com.google.auto.value.AutoValue;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class C
        extends Key {
    @Override
    public int layout() {
        return R.layout.path_c;
    }

    public static C create() {
        return new AutoValue_C();
    }
}
