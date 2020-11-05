package com.viet.mine.activity

import android.os.Bundle
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.chenenyu.router.annotation.Route
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener
import com.viet.mine.R
import com.viet.mine.adapter.CollectionAdapter
import com.viet.mine.viewmodel.CollectionViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.User
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.isBlank
import com.viet.news.core.ui.InjectActivity
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.activity_mine_collection.*
import javax.inject.Inject

/**
 * @Description 用户收藏
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_COLLECTION_ACTIVITY], interceptors = [(Config.LOGIN_INTERCEPTOR)])
class CollectionActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: CollectionAdapter
    private val model: CollectionViewModel by viewModelDelegate(CollectionViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_collection)
        initView()
        initListener()
        refreshLayout.autoRefresh()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        adapter.setClickDelegate {
            onItemClick = { url -> WebActivity.launch(this@CollectionActivity, url) }
            onItemDelete = { id -> model.collection(this@CollectionActivity, id) }
        }
        refreshLayout.setOnMultiPurposeListener(object : OnMultiPurposeListener {
            override fun onFooterMoving(footer: RefreshFooter?, isDragging: Boolean, percent: Float, offset: Int, footerHeight: Int, maxDragHeight: Int) {
                adapter.closeAllItem()
            }

            override fun onHeaderStartAnimator(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {
            }

            override fun onFooterReleased(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {
            }

            override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
            }

            override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
                adapter.closeAllItem()
            }

            override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
            }

            override fun onFooterStartAnimator(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {
            }

            override fun onHeaderReleased(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
            }

            override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
            }
        })
    }

    private fun initView() {
        rl_collection.adapter = adapter
        rl_collection.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rl_collection.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    adapter.closeAllItem()
                }
            }
        })
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            model.page_number += 1
        } else {
            model.page_number = 1
        }
        model.getCollectionList(User.currentUser.userId).observe(this, Observer { it ->
            it?.work(onSuccess = {
                multiStatusView.showContent()
                if (loadMore) {
                    if (isBlank(it.data?.list)) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore()
                        adapter.addData(it.data?.list)
                    }
                } else {
                    if (isBlank(it.data?.list)) {
                        multiStatusView.showEmpty()
                        refreshLayout.setEnableLoadMore(false)
                    }
                    adapter.setData(it.data?.list as ArrayList<NewsListBean>)
                    refreshLayout.setNoMoreData(false)
                    refreshLayout.finishRefresh()
                }
            }, onError = {
                multiStatusView.showError()
                if (loadMore) {
                    refreshLayout.finishLoadMore(false)//传入false表示加载失败
                } else {
                    refreshLayout.finishRefresh(false)
                }
            })
        })
    }
}