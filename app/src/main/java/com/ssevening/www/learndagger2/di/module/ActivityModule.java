package com.ssevening.www.learndagger2.di.module;

import android.app.Activity;


import dagger.Module;
import dagger.Provides;

/**
 * Created by ssevening on 2017/4/19.
 */

@Module
public class ActivityModule {
    private Activity activity;

    /**
     * 构造函数,初始化的时候，要传入一个Activity
     *
     * @param activity
     */
    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * 这里声明能力，可以提供Activity的能力
     *
     * @return
     */
    @Provides
    public Activity provideActivity() {
        return activity;
    }
}
