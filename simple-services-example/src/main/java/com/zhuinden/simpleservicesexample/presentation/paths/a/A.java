package com.zhuinden.simpleservicesexample.presentation.paths.a;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.application.Key;

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
