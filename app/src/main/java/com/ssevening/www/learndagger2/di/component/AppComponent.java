package com.ssevening.www.learndagger2.di.component;

import android.app.Activity;
import android.app.Application;

import com.ssevening.www.learndagger2.service.CleanService;
import com.ssevening.www.learndagger2.service.TeachService;
import com.ssevening.www.learndagger2.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 定义一个App组别的组件,对外提供服务
 * Created by Pan on 2017/4/19.
 * App 的第一个组件
 */
@Singleton // Application级别的组件，所以是单例
@Component(modules = ApplicationModule.class) // 这个组件就职于Application module, 所以获取组件的时候，
// 也要告知其依赖的Modules
public interface AppComponent {

    void inject(Activity activity);

    // 可以获取到Application的上下文
    Application provideApplication();

    /**
     * 这个组件有保洁服务, 这里的服务都可以理解为：DB读写啊，Http数据获取之类的
     *
     * @return
     */
    CleanService getCleanService();

    /**
     * 这个组件有教育服务,同上，这里的服务都是换为其他服务的。
     *
     * @return
     */
    TeachService getTeachService();

}
