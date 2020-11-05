package com.viet.news.di

import android.app.Activity
import com.squareup.leakcanary.LeakCanary
import com.viet.news.core.ui.App
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * @Author Aaron
 * @Date 2018/5/7
 * @Email aaron@magicwindow.cn
 * @Description
 */
class NewsApp : App(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        AppInjector.init(this)
        //方法已过期 看注释说是会自动调用，暂时不删
//        FacebookSdk.sdkInitialize(applicationContext);
//        AppEventsLogger.activateApp(this);
    }


    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

}