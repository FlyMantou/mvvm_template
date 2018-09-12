package com.viet.news.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import com.viet.news.core.R
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.BaseActivity

/**
 * @Description webveiw
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/01/2018 5:24 PM
 * @Version 1.0.0
 */
//@Route(value = [(Config.ROUTER_WEBVIEW_ACTIVITY)], interceptors = [(Config.LOGIN_INTERCEPTOR)])
class WebActivity : BaseActivity() {

    private lateinit var mAgentWebFragment: AgentWebFragment
    private val model by viewModelDelegate(WebviewViewModel::class)
    private var mUrl: String? = null
    private var mTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //webview截取长图的时候出现白屏bug修复
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }
        setContentView(R.layout.activity_common)
        AndroidBug5497Helper.assistActivity(this)
        initData()
        initView()
    }

    private fun initData() {
        mUrl = intent?.getStringExtra(URL)
        mTitle = intent?.getStringExtra(TITLE)
        model.injectedName.value = intent?.getStringExtra(INJECTED_NAME)

        //自有域名默认添加android接口。
        if (mUrl != null) {
            val host = Uri.parse(mUrl).host
            if (!host.isNullOrEmpty() && (host.contains(HOST, false)
                            || host.contains(HOST1, false)
                            || host.contains(HOST2, false)
                            || host.contains(HOST3, false))) {
                model.injectedName.value = INJECTED_NAME_DEFAULT
            }
        }
    }

    private fun initView() {
        val fragmentManager = this.supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        val mBundle = Bundle()
        mAgentWebFragment = if (model.injectedName.value.isNullOrEmpty()) {
            AgentWebFragment.getInstance(mBundle)
        } else {
            JSAgentWebFragment.getInstance(mBundle)
        }
        ft.add(R.id.container_framelayout, mAgentWebFragment, AgentWebFragment::class.java.name)
        mBundle.putString(URL, mUrl)
        mBundle.putString(TITLE, mTitle)
        ft.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mAgentWebFragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val url = mAgentWebFragment.mAgentWeb?.webCreator?.webView?.url
        if (url.isNullOrBlank() || url!!.contains("app-index")) finish()   //url空指针修复
        val mAgentWebFragment = this.mAgentWebFragment
        return if (mAgentWebFragment.onFragmentKeyDown(keyCode, event))
            true
        else
            super.onKeyDown(keyCode, event)
    }

    companion object {

        const val URL = "url"
        const val TITLE = "title"
        const val INJECTED_NAME = "injectedName"
        internal const val HOST = "merculet.io"
        internal const val HOST1 = "magicwindow.cn"
        internal const val HOST2 = "liaoyantech.cn"
        internal const val HOST3 = "merculet.cn"
        internal const val INJECTED_NAME_DEFAULT = "android"
        const val LANGUAGE_COUNTRY = "lang"

        fun launch(context: Context?, url: String?, title: String? = null, injectedName: String? = null, skipInterceptor: Boolean = true) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(TITLE, title)
            intent.putExtra(INJECTED_NAME, injectedName)
            context?.startActivity(intent)
        }
    }
}
