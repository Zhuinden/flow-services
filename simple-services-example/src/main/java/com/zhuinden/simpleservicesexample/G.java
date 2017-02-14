package com.zhuinden.simpleservicesexample;

import com.google.auto.value.AutoValue;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class G
        extends Key {
    @Override
    public int layout() {
        return R.layout.path_g;
    }

    public static G create() {
        return new AutoValue_G();
    }
}
