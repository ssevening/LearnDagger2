package com.ssevening.www.learndagger2.app;

import android.app.Application;

import com.ssevening.www.learndagger2.di.component.AppComponent;
import com.ssevening.www.learndagger2.di.component.DaggerAppComponent;
import com.ssevening.www.learndagger2.di.module.ApplicationModule;

/**
 * Created by Pan on 2017/4/19.
 */

public class MyApplication extends Application {
    /**
     * 在Application中声明我们要用的组件
     */
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // 这样就完成了appComponent的初始化工具,然后我们通过增加 getAppComponent 把组件能力开放给整个App使用
        appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
