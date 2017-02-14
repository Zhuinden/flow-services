package com.zhuinden.simpleservices;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zhuinden.simplestack.Backstack;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zhuinden on 2017.02.14..
 */

public class BView
        extends RelativeLayout {
    public BView(Context context) {
        super(context);
    }

    public BView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @BindView(R.id.b_viewpager)
    ViewPager viewPager;

    @BindView(R.id.b_viewpager_2)
    ViewPager viewPager2;

    ViewPagerAdapter adapter;
    ViewPagerAdapter adapter2;

    List<Key> keys;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        MainActivity.getServices(getContext()).findServices(Backstack.getKey(getContext())).getService("B");
        ButterKnife.bind(this);
        B b = Backstack.getKey(getContext());
        keys = b.<Key>keys();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager2.setAdapter(null);
                adapter2.updateKeys(getNestedKeys(keys.get(position)));
                viewPager2.setAdapter(adapter2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new ViewPagerAdapter(keys);

        adapter2 = new ViewPagerAdapter(Collections.emptyList());
        viewPager.setAdapter(adapter = new ViewPagerAdapter(keys));
    }

    List<Key> getNestedKeys(Key key) {
        if(key instanceof Services.Composite) {
            return ((Services.Composite<Key>) key).keys();
        } else {
            return Collections.emptyList();
        }
    }
}
