package com.zhuinden.simpleservicesexample.presentation.paths.i;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservices.Services;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.application.Key;

/**
 * Created by Owner on 2017. 02. 17..
 */

@AutoValue
public abstract class I
        extends Key
        implements Services.Child {
    public abstract Key parent();

    @Override
    public int layout() {
        return R.layout.path_i;
    }

    public static I create(Key parent) {
        return new AutoValue_I(parent);
    }
}
