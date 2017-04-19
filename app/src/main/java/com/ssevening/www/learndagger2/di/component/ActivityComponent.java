package com.ssevening.www.learndagger2.di.component;

import android.app.Activity;

import com.ssevening.www.learndagger2.di.module.ActivityModule;
import com.ssevening.www.learndagger2.di.scope.PerActivity;


import dagger.Component;

/**
 * Created by Pan on 2017/4/19.
 * App 的第二个Activity组别的控件
 */
@PerActivity // Activity组别的组件，单例肯定不行，所以一个Activity生成一个组件
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    /**
     * 这个组件就只有获取Activity的方法
     *
     * @return
     */
    Activity getActivity();
}
