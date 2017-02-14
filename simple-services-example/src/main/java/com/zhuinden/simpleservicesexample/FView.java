package com.zhuinden.simpleservicesexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zhuinden.simplestack.Backstack;

/**
 * Created by Zhuinden on 2017.02.14..
 */

public class FView
        extends RelativeLayout {
    public FView(Context context) {
        super(context);
    }

    public FView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public FView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        MainActivity.getServices(getContext()).findServices(Backstack.getKey(getContext())).getService("F");
    }
}
