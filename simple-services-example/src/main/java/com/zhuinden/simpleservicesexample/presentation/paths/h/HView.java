package com.zhuinden.simpleservicesexample.presentation.paths.h;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zhuinden.simpleservices.ServicesManager;
import com.zhuinden.simpleservicesexample.application.MainActivity;

/**
 * Created by Owner on 2017. 02. 16..
 */

public class HView
        extends RelativeLayout {
    public HView(Context context) {
        super(context);
    }

    public HView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ServicesManager servicesManager = MainActivity.getServices(getContext());
    }
}
