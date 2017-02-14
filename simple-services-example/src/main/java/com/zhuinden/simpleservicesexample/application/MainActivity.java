package com.zhuinden.simpleservicesexample.application;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.zhuinden.simpleservices.Services;
import com.zhuinden.simpleservices.ServicesFactory;
import com.zhuinden.simpleservices.ServicesManager;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.presentation.paths.a.A;
import com.zhuinden.simpleservicesexample.presentation.paths.b.B;
import com.zhuinden.simpleservicesexample.presentation.paths.b.c.C;
import com.zhuinden.simpleservicesexample.presentation.paths.b.d.D;
import com.zhuinden.simpleservicesexample.presentation.paths.b.d.f.F;
import com.zhuinden.simpleservicesexample.presentation.paths.b.d.g.G;
import com.zhuinden.simpleservicesexample.presentation.paths.b.e.E;
import com.zhuinden.simpleservicesexample.utils.StackService;
import com.zhuinden.simplestack.BackstackDelegate;
import com.zhuinden.simplestack.HistoryBuilder;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements StateChanger {

    @BindView(R.id.root)
    RelativeLayout root;

    BackstackDelegate backstackDelegate;

    ServicesManager servicesManager;

    public static class NonConfigurationInstance {
        ServicesManager servicesManager;
        BackstackDelegate.NonConfigurationInstance backstackDelegateNonConfig;
    }

    public static final String SERVICES = "SERVICES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NonConfigurationInstance nonConfigurationInstance = (NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        if(nonConfigurationInstance == null) {
            List<ServicesFactory> servicesFactories = new ArrayList<>();
            servicesFactories.add(new ServicesFactory() {
                @Override
                public void bindServices(@NonNull Services.Builder builder) {
                    if(builder.getKey() instanceof A) {
                        builder.withService("A", "A");
                    } else if(builder.getKey() instanceof B) {
                        builder.withService("B", "B");
                    } else if(builder.getKey() instanceof C) {
                        builder.withService("C", "C");
                    } else if(builder.getKey() instanceof D) {
                        builder.withService("D", "D");
                    } else if(builder.getKey() instanceof E) {
                        builder.withService("E", "E");
                    } else if(builder.getKey() instanceof F) {
                        builder.withService("F", "F");
                    } else if(builder.getKey() instanceof G) {
                        builder.withService("G", "G");
                    }
                }
            });
            servicesManager = new ServicesManager(servicesFactories);
        } else {
            servicesManager = nonConfigurationInstance.servicesManager;
        }

        backstackDelegate = new BackstackDelegate(this);
        backstackDelegate.onCreate(savedInstanceState,
                nonConfigurationInstance == null ? null : nonConfigurationInstance.backstackDelegateNonConfig,
                HistoryBuilder.single(A.create()));
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        NonConfigurationInstance nonConfigurationInstance = new NonConfigurationInstance();
        nonConfigurationInstance.backstackDelegateNonConfig = backstackDelegate.onRetainCustomNonConfigurationInstance();
        nonConfigurationInstance.servicesManager = servicesManager;
        return nonConfigurationInstance;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        backstackDelegate.onPostResume();
    }

    @Override
    public void onBackPressed() {
        if(!backstackDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        backstackDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        backstackDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        backstackDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        if(SERVICES.equals(name)) {
            return servicesManager;
        }
        if(StackService.TAG.equals(name)) {
            return backstackDelegate.getBackstack();
        }
        return super.getSystemService(name);
    }

    public static ServicesManager getServices(Context context) {
        // noinspection ResourceType
        return (ServicesManager) context.getSystemService(SERVICES);
    }

    @Override
    public void handleStateChange(StateChange stateChange, Callback completionCallback) {
        if(stateChange.topNewState().equals(stateChange.topPreviousState())) {
            completionCallback.stateChangeComplete();
            return;
        }
        for(Parcelable newKey : stateChange.getNewState()) {
            if(!servicesManager.hasServices(newKey)) {
                servicesManager.setUp(newKey);
            }
        }
        for(int i = stateChange.getPreviousState().size() - 1; i >= 0; i--) {
            Parcelable previousKey = stateChange.getPreviousState().get(i);
            if(servicesManager.hasServices(previousKey) && !stateChange.getNewState().contains(previousKey)) {
                servicesManager.tearDown(previousKey);
            }
        }
        backstackDelegate.persistViewToState(root.getChildAt(0));
        root.removeAllViews();
        Key newKey = stateChange.topNewState();
        Context newContext = stateChange.createContext(this, newKey);
        View view = LayoutInflater.from(newContext).inflate(newKey.layout(), root, false);
        backstackDelegate.restoreViewFromState(view);
        root.addView(view);
        completionCallback.stateChangeComplete();
    }
}
