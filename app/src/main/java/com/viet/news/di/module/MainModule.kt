package com.viet.news.di.module

import androidx.fragment.app.FragmentManager
import com.viet.news.core.di.ActivityScope
import com.viet.news.ui.activity.MainActivity
import dagger.Module
import dagger.Provides

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 3:58 PM
 * @Version
 */
@Module
class MainModule {

    @ActivityScope
    @Provides
    internal fun providesMainActivity(activity: MainActivity): FragmentManager = activity.supportFragmentManager
}