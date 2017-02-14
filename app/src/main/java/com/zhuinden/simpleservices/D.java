package com.zhuinden.simpleservices;

import com.google.auto.value.AutoValue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhuinden on 2017.02.14..
 */

@AutoValue
public abstract class D
        extends Key
        implements Services.Composite<Key> {
    @Override
    public int layout() {
        return R.layout.path_d;
    }

    public static D create() {
        return new AutoValue_D();
    }

    @Override
    public List<Key> keys() {
        return Arrays.asList(F.create(), G.create());
    }
}
