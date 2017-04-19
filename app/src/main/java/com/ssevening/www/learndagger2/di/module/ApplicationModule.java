package com.ssevening.www.learndagger2.di.module;

import android.app.Application;

import com.ssevening.www.learndagger2.service.CleanService;
import com.ssevening.www.learndagger2.service.CleanServiceImpl;
import com.ssevening.www.learndagger2.service.TeachService;
import com.ssevening.www.learndagger2.service.TeachServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pan on 2017/4/19.
 */

@Module
public class ApplicationModule {

    private final Application application;

    /**
     * 初始化ApplicationMoudle时的构造函数和传参
     *
     * @param application
     */
    public ApplicationModule(Application application) {
        this.application = application;
    }

    /**
     * 首先我们对外提供Application的上下文
     *
     * @return
     */
    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }

    /**
     * 然后我们还对外提供保洁服务
     */
    @Provides // 加上Provides注解，同时声明为单例
    @Singleton
    CleanService provideCleanService() {
        return new CleanServiceImpl();
    }

    /**
     * 继续，我们再提供教育服务
     */
    @Provides
    @Singleton
    TeachService provideTeachService() {
        return new TeachServiceImpl();
    }
}
