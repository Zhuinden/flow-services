package com.zhuinden.simpleservices;

import com.google.auto.value.AutoValue;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class A
        extends Key {
    @Override
    public int layout() {
        return R.layout.path_a;
    }

    public static A create() {
        return new AutoValue_A();
    }
}
