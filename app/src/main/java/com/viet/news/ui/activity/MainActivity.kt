package com.viet.news.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import com.chenenyu.router.annotation.Route
import com.safframework.ext.clickWithTrigger
import com.viet.news.R
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.RefreshNewsEvent
import com.viet.news.core.dsl.addOnPageChangeListener
import com.viet.news.core.dsl.setOnTabSelectListener
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import com.viet.news.core.utils.LanguageUtil
import com.viet.news.core.utils.RxBus
import com.viet.news.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@Route(value = [(Config.ROUTER_MAIN_ACTIVITY)])
class MainActivity : InjectActivity() {

    @Inject
    internal lateinit var pagerAdapter: TabFragmentAdapter
    private val model: MainViewModel by viewModelDelegate(MainViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        pagerAdapter.setFragment(model.fragments)
        pagerAdapter.setTitles(model.titles)
        pagerAdapter.notifyDataSetChanged()
        container.adapter = pagerAdapter
        bottomBar.setTabData(model.tabEntities)

        container.offscreenPageLimit = 3//缓存3个界面
        container.addOnPageChangeListener { onPageSelected = { bottomBar.currentTab = it } }
        bottomBar.setOnTabSelectListener { onTabSelect = { container.setCurrentItem(it, false) } }
        bottomBar.getIconView(0).clickWithTrigger {
            if (bottomBar.currentTab == 0) {
                RxBus.get().post(RefreshNewsEvent())
            } else {
                container.setCurrentItem(0, false)
            }
        }
    }

    /*
    * 当配置信息改变而不需要重建activity时调用，配置信息在manifest文件中
    * 例如切换系统语言，针对我们的App，主页也没用keyboard事件，基本上就是切换系统语言才会调用此方法
    */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageUtil.onConfigurationChanged(this)
        reLoadView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val languageChanged = intent?.extras?.getBoolean(Config.LANGUAGE_CHANGED)
        languageChanged?.let {
            if (it) {
                LanguageUtil.onConfigurationChanged(this)
                reLoadView()
            }
        }
    }

    // tsing 在App内切换语言 需要重新加载当前界面
    private fun reLoadView() {
        pagerAdapter = TabFragmentAdapter(supportFragmentManager)
        model.reLoadData()
        initView()
        bottomBar.currentTab = 0
    }

    /**
     * 双击返回键退出
     */
    private var mIsExit: Boolean = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                container.currentItem != 0 -> container.currentItem = 0
                mIsExit -> this.finish()
                else -> {
                    toast(R.string.exit_by_double_click)
                    mIsExit = true
                    Handler().postDelayed(Runnable { mIsExit = false }, 2000)
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
