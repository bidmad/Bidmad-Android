package com.adop.example.adopsample.Consumable;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public final class RootActivityTracker {
    public interface Listener {
        void onRootDestroyed();
        void onRootRecreated(@NonNull Activity newRoot);
    }

    private static final String TAG = "RootActivityTracker";
    private static volatile RootActivityTracker instance;

    private final Application.ActivityLifecycleCallbacks callbacks;
    private WeakReference<Activity> currentRoot = new WeakReference<>(null);
    private String rootClassName;
    @Nullable
    private Listener listener;

    private RootActivityTracker(@NonNull Activity currentActivity) {
        currentRoot = new WeakReference<>(currentActivity);
        rootClassName = currentActivity.getClass().getName();
        Application application = currentActivity.getApplication();
        this.callbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (!activity.isTaskRoot()) return;
                onRootCreated(activity);
            }

            @Override public void onActivityStarted(@NonNull Activity activity) {}
            @Override public void onActivityResumed(@NonNull Activity activity) {}
            @Override public void onActivityPaused(@NonNull Activity activity) {}
            @Override public void onActivityStopped(@NonNull Activity activity) {}
            @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Activity current = currentRoot.get();
                if (current != null && current == activity) {
                    onRootDestroyed(activity);
                }
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
        Log.d(TAG, "installed on " + application.getClass().getSimpleName());
    }

    @NonNull
    public static RootActivityTracker shared(@NonNull Activity currentActivity) {
        RootActivityTracker local = instance;
        if (local == null) {
            synchronized (RootActivityTracker.class) {
                local = instance;
                if (local == null) {
                    local = new RootActivityTracker(currentActivity);
                    instance = local;
                }
            }
        }
        return local;
    }

    public synchronized void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Nullable
    public Activity getCurrentRoot() {
        return currentRoot.get();
    }

    private synchronized void onRootCreated(Activity activity) {
        String className = activity.getClass().getName();
        boolean isRecreation = rootClassName != null
                && currentRoot.get() == null
                && className.equals(rootClassName);
        currentRoot = new WeakReference<>(activity);
        rootClassName = className;
        if (isRecreation && listener != null) {
            Log.d(TAG, "root RECREATED: " + className
                    + "@" + Integer.toHexString(System.identityHashCode(activity)));
            listener.onRootRecreated(activity);
        }
    }

    private synchronized void onRootDestroyed(Activity activity) {
        String hex = "@" + Integer.toHexString(System.identityHashCode(activity));
        Log.d(TAG, "root DESTROYED: " + activity.getClass().getName() + hex);
        currentRoot.clear();
        if (listener != null) {
            listener.onRootDestroyed();
        }
    }
}
