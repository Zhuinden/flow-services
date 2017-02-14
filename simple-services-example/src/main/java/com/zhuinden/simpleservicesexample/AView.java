package com.zhuinden.simpleservicesexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhuinden.simplestack.Backstack;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zhuinden on 2017.02.14..
 */

public class AView
        extends RelativeLayout {
    public AView(Context context) {
        super(context);
    }

    public AView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @OnClick(R.id.a_button)
    public void buttonClick(View view) {
        StackService.get(view.getContext()).goTo(B.create(Backstack.getKey(getContext())));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        MainActivity.getServices(getContext()).findServices(Backstack.getKey(getContext())).getService("A");
    }
}
