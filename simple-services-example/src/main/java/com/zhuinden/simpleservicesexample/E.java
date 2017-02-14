package com.zhuinden.simpleservicesexample;

import com.google.auto.value.AutoValue;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class E
        extends Key {
    @Override
    public int layout() {
        return R.layout.path_e;
    }

    public static E create() {
        return new AutoValue_E();
    }
}
