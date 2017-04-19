package com.ssevening.www.learndagger2.view;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ssevening.www.learndagger2.R;
import com.ssevening.www.learndagger2.app.MyApplication;
import com.ssevening.www.learndagger2.di.component.DaggerMainActivityComponent;
import com.ssevening.www.learndagger2.di.component.MainActivityComponent;
import com.ssevening.www.learndagger2.di.module.ActivityModule;
import com.ssevening.www.learndagger2.service.CleanService;
import com.ssevening.www.learndagger2.service.TeachService;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    MainActivityComponent mainActivityComponent;
    @Inject
    TeachService teachService;
    @Inject
    CleanService cleanService;
    @Inject
    Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityComponent = DaggerMainActivityComponent.builder().appComponent(getMyApp().getAppComponent()).activityModule(new ActivityModule(this)).build();
        // 这里执行织入，即建立联系,并告诉家政公司，挂着Inject的,需要服务一下,用到inject的话，那组件中必须有inject方法。
        // 同理,如果用到了@ Inject注解,那必须要调用:mainActivityComponent.inject(this);
        mainActivityComponent.inject(this);


        // 上面建立联系以后，我们就可以直接享受保洁和教育服务了

        String teachSay = teachService.teach();
        String cleanSay = cleanService.clean();
        Toast.makeText(this, teachSay, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, cleanSay, Toast.LENGTH_SHORT).show();

        // application也可以直接获取了。所以MainActivityComponent
        // 从组件上要去依赖然后才可以获取到AppComponent，然后才可以获取到AppComponent的方法
        application.getSystemService(ALARM_SERVICE);

        // 当然也可以直接调用 ActivityComponent的getActivity方法。
        mainActivityComponent.getActivity();


        /**
         * 最后，我们再来总结一下。
         * 1. 首先，我们定义了系统级别组件AppComponent，比如获取Application,获取 CleanService 和 TeachService.
         * 2. 然后我们定义Activity级别的组件，比如可以获取到当前Activity
         * 3. 然后定义MainActivity级别的组件，只提供inject方法。
         * component定义到此完成
         *
         * 4. 再定义相应 家政公司，即Module. 对外声明一些服务，然后留下联系方式:inject方法。
         *
         * 5. 然后需要用到CleanService和teachService的地方，只需要和Module建立联系 inject,然后再打上 @inject标签。
         * dagger就会自动把相应的服务给注入进来，供我们享受服务。
         *
         * 所以综上所述，Dagger2 的主要用处就是把使用者和实现分开，并且只调用接口，
         * 所以这个时候，我们的网络请求，数据库读写，以及获取一些实现时，抽出相应的公共接口，然后下面的实现就可以随便切换而不用修改业务代码了。
         * 所谓的：给开着的分机换引擎，就是这样换的。
         *
         * 注：@Singleton 和 @PerActivity 都属于Scope，所以不要双重定定。
         *
         *
         */


    }

    public MyApplication getMyApp() {
        // 这里为了代码更直接，所以直接用的强制类型转换，可以考虑定义在BaseActivity之类的
        return (MyApplication) getApplication();
    }
}
