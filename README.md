

# 安卓 Android Dagger2 学习 使用文档

介绍前，我们先来讲一个故事：
1. 有能力提供保洁和家教服务的人。
说有一批保洁人员：CleanComponent和一个家教人员： TeacherComponent，一个提供保洁服务:cleanService，一个提供家教服务:TeachService。

2. 家政公司把有能力提供服务的人招进来，然后作为自己的营业内容对外输出。
然后我们组建一个家政中心：HomeHelpModule，对外提供保洁和家教服务, 然后还会在橱窗上贴上@Provide的大标签，即：provideCleanService 和 provideTeachService。

3. 有家庭需要保洁和家教服务
需要服务的家庭就和家政公司建立关系【织入】inject，然后在自己门上贴：@Inject cleanService 和 @Inject teachService，
HomeHelpModule就会安排人员上门，家庭享用保洁和家教服务，

4. 家教服务只有老板娘的一个大学生女儿可以提供服务，但保洁阿姨有很多，所以，保洁Scope就是每个家庭分配一个PerActivity。家教服务的话，只能是单例@Singleton了。
所以指定服务人员的Scope,来解决多家庭需要时，需要多少个服务者的问题。但对于使用者来说，他要做的只是贴个牌子，和家政公司建立联系，其他的事情，就是享受保洁和家教服务。

如果听懂了上面的故事，那基本Dagger的思想也就了解了。


# 一、**Dagger2** 添加依赖

* 1.工程build.gradle添加apt依赖

```

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4' // 这里添加自定义注解依赖

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

```

* 2.App中的 build.gradle添加 Dagger依赖

```

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:25.3.1'
    compile 'com.android.support.constraint:constraint-layout:1.0.1'
    // 这里加入注解编译器
    apt 'com.google.dagger:dagger-compiler:2.0' //注解编译器
    compile 'org.glassfish:javax.annotation:10.0-b28' // 注解
    // 加入dagger的依赖
    compile 'com.google.dagger:dagger:2.0' //Dagger依赖
    testCompile 'junit:junit:4.12'
}

```

# 二、**Dagger2** 实际开发，直接撸代码

 > 首先再说一下需求，我们的需求就是建立一个全局可以供调用的保洁服务和家教服务在Android工程中。

* 1.首先在建立 AppComponent

```


/**
 * 定义一个App组别的组件,对外提供服务
 * Created by ssevening on 2017/4/19.
 * App 的第一个组件
 */
@Singleton // Application级别的组件，所以是单例
@Component(modules = ApplicationModule.class) // 这个组件就职于 Application module, 所以获取组件的时候，
// 也要告知其依赖的Modules
public interface AppComponent {
    // 织入方法
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

```

* 2.建立ApplicationModule，即上面组件工作的地方.

```

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
    @Provides // 加上Provides注解,保不用保持单例，因为有很多保洁
    CleanService provideCleanService() {
        return new CleanServiceImpl();
    }

    /**
     * 继续，我们再提供教育服务
     */
    @Provides
    @Singleton // 只有老板娘一个女儿提供服务，没办法，要单例
    TeachService provideTeachService() {
        return new TeachServiceImpl();
    }
}


```

* 3.建立ActivityComponent组件，提供getActivity方法。

```
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

```

* 4.同时创建ActivityModule代码如下：

```
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


```

* 5. 终于要到主角登场了，建立MainActivityComponent组件，只用来织用，不提供其他功能。

```
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

```

* 6.同理这里建个空的MainActivityModule

```
@Module
public class MainActivityModule {

}

```

* 7.上面已经完成了所有的提供服务人的定义和家政公司的定义，并在家政公司玻璃上贴了上提供服务的广告。下面就是把家政公司和实际使用的家庭关联在一起的时候了。首先，因为是全局服务，我们要先初始化Application.

```

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

    /**
     * 获得我们的AppComponent组件，以对外提供服务
     * @return
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }


}

```

* 8.然后真正的主角到了，MainActivity。可以看到，mainActivityComponent公司通过融合AppComponent和ActivityModule的业务后，然后调用inject进行关联织入，织入完成后，就可以直接调用了。

```

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

```

* 9.最后，关于CleanService和TeachService，只是接口定义，所以家庭获取到服务后，并不知具体的实现，后期具体实现也可以直接替换。最终完成了解耦的目的，不会再出现换个网络组件或工具类，要修改大量业务代码的问题。


源码地址：[https://github.com/ssevening/LearnDagger2](https://github.com/ssevening/LearnDagger2)

附上个人微信公众号，欢迎交流。

![个人微信公众号](https://ssevening.github.io/assets/weichat_qrcode.jpg)






