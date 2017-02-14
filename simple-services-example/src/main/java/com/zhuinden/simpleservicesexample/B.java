package com.zhuinden.simpleservicesexample;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservices.Services;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class B
        extends Key
        implements Services.Composite, Services.Child {
    @Override
    public int layout() {
        return R.layout.path_b;
    }

    public static B create(A parent) {
        return new AutoValue_B(parent);
    }

    @Override
    public List<Key> keys() {
        return Arrays.asList(C.create(), D.create(), E.create());
    }
}
