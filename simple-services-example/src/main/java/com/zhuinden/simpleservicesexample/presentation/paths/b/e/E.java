package com.zhuinden.simpleservicesexample.presentation.paths.b.e;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.application.Key;

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
