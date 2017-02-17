package com.zhuinden.simpleservicesexample.presentation.paths.j.n;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservicesexample.application.Key;

/**
 * Created by Owner on 2017. 02. 17..
 */

@AutoValue
public abstract class N
        extends Key {
    @Override
    public int layout() {
        return 0;
    }

    public static N create() {
        return new AutoValue_N();
    }
}
