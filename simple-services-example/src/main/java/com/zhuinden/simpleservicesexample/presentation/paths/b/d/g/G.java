package com.zhuinden.simpleservicesexample.presentation.paths.b.d.g;

import com.google.auto.value.AutoValue;
import com.zhuinden.simpleservices.Services;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.application.Key;

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

    @Override
    public void bindServices(Services.Builder builder) {
        builder.withService("G", "G");
    }
}
