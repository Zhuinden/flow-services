package com.zhuinden.simpleservicesexample.application;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhuinden.simpleservices.ServiceFactory;
import com.zhuinden.simpleservices.ServiceManager;
import com.zhuinden.simpleservices.Services;
import com.zhuinden.simpleservicesexample.R;
import com.zhuinden.simpleservicesexample.presentation.paths.a.A;
import com.zhuinden.simpleservicesexample.utils.StackService;
import com.zhuinden.simplestack.BackstackDelegate;
import com.zhuinden.simplestack.HistoryBuilder;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements StateChanger {
    private static final String TAG = "MainActivity";

    @BindView(R.id.root)
    RelativeLayout root;

    BackstackDelegate backstackDelegate;

    ServiceManager serviceManager;

    public static class NonConfigurationInstance {
        ServiceManager serviceManager;
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
            serviceManager = ServiceManager.configure().addServiceFactory(new ServiceFactory() {
                @Override
                public void bindServices(@NonNull Services.Builder builder) {
                    Key key = builder.getKey();
                    key.bindServices(builder);
                }

                @Override
                public void tearDownServices(@NonNull Services services) {
                    Log.i("ServiceManager", "<[Tearing down :: " + services.getKey() + "]>");
                }
            }).build();
        } else {
            serviceManager = nonConfigurationInstance.serviceManager;
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
        nonConfigurationInstance.serviceManager = serviceManager;
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
            return serviceManager;
        }
        if(StackService.TAG.equals(name)) {
            return backstackDelegate.getBackstack();
        }
        return super.getSystemService(name);
    }

    public static ServiceManager getServices(Context context) {
        // noinspection ResourceType
        return (ServiceManager) context.getSystemService(SERVICES);
    }

    @Override
    public void handleStateChange(StateChange stateChange, Callback completionCallback) {
        Log.i(TAG,
                Arrays.toString(stateChange.getPreviousState().toArray()) + " :: " + Arrays.toString(stateChange.getNewState().toArray()));

        serviceManager.dumpLogData();
        Parcelable topNewKey = stateChange.topNewState();
        boolean isInitializeStateChange = stateChange.getPreviousState().isEmpty();
        boolean servicesUninitialized = (isInitializeStateChange && !serviceManager.hasServices(topNewKey));
        if(servicesUninitialized || !isInitializeStateChange) {
            serviceManager.setUp(topNewKey);
            Log.i(TAG, "<< Restore [" + topNewKey + "] >>");
            // TODO: RESTORE CHILD HIERARCHY (with setUp?)
        }
        for(int i = stateChange.getPreviousState().size() - 1; i >= 0; i--) {
            Parcelable previousKey = stateChange.getPreviousState().get(i);
            if(serviceManager.hasServices(previousKey) && !stateChange.getNewState().contains(previousKey)) {
                Log.i(TAG, "<< Destroy [" + previousKey + "] >>");
                serviceManager.tearDown(previousKey);
                // TODO: DESTROY
            }
        }
        Parcelable topPreviousKey = stateChange.topPreviousState();
        if(topPreviousKey != null && stateChange.getNewState().contains(topPreviousKey)) {
            Log.i(TAG, "<< Persist [" + topPreviousKey + "] >>");
            // TODO: PERSIST CHILD HIERARCHY (with tearDown?)
            serviceManager.tearDown(topPreviousKey);
        }

        serviceManager.dumpLogData();

        if(stateChange.topNewState().equals(stateChange.topPreviousState())) {
            completionCallback.stateChangeComplete();
            return;
        }

        backstackDelegate.persistViewToState(root.getChildAt(0));
        root.removeAllViews();
        Key newKey = stateChange.topNewState();
        Context newContext = stateChange.createContext(this, newKey);
        View view = LayoutInflater.from(newContext).inflate(newKey.layout(), root, false);
        backstackDelegate.restoreViewFromState(view);
        root.addView(view);
        completionCallback.stateChangeComplete(); // TODO: CLEAR STATES NOT IN
    }
}
