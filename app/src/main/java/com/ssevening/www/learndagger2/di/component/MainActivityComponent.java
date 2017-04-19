package com.ssevening.www.learndagger2.di.component;


import com.ssevening.www.learndagger2.view.MainActivity;
import com.ssevening.www.learndagger2.di.module.ActivityModule;
import com.ssevening.www.learndagger2.di.module.MainActivityModule;
import com.ssevening.www.learndagger2.di.scope.PerActivity;


import dagger.Component;

/**
 * Created by Pan on 2017/4/19.
 * App 中的第三个控件,这个控件是为MainActivity添加的,需求就是在mainActivity中，需要清洁服务和教育服务.
 * 但清洁服务和教育服务，要在Application中去获取,所以组件上我们依赖于AppComponent的能力。
 * 同时，这个组件还是Activity的一个子类
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {MainActivityModule.class, ActivityModule.class})
public interface MainActivityComponent extends ActivityComponent {
    /**
     * 这个组件不提供什么能力,只用于织入就行了
     * 所以我们这里只写一个默认的织入方法
     */
    void inject(MainActivity mainActivity);
}
