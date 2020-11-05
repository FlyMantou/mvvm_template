package com.viet.news.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import cn.magicwindow.channelwidget.AddChannelFragment
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.jaeger.library.StatusBarUtil
import com.safframework.ext.clickWithTrigger
import com.viet.follow.fragment.NewsFragment
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.R
import com.viet.news.core.config.NetWorkState
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.LogoutEvent
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.InjectFragment
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.activity_find.*
import javax.inject.Inject

/**
 * @Description 发现
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FindFragment : InjectFragment(), AddChannelFragment.DataChangeListener {
    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_find
    }

    @Inject
    internal lateinit var pagerAdapter: MyViewPager
    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)

    private var mAddChannelFragment: AddChannelFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, 30, null)
        initData()
        initEvent()
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(LogoutEvent::class.java) { initData() })
        compositeDisposable.add(RxBus.get().register(LoginEvent::class.java) { initData() })
        NetWorkState(context).observe(this, Observer { if (it!!) initData() })  //有网络之后重新加载
    }

    private fun initData() {
        model.getChannelAllList(this)
        model.getChannelList(this) { update(model.normalList) }
    }

    override fun initView(view: View) {
        id_view_Pager.offscreenPageLimit = 5
        id_view_Pager.adapter = pagerAdapter
        id_tab_pager_indicator.setupWithViewPager(id_view_Pager)

        id_add_channel_entry_iv.clickWithTrigger {
            mAddChannelFragment = AddChannelFragment(model.followList, model.unFollowList)
            mAddChannelFragment?.setOnDataChangeListener(this)
            mAddChannelFragment?.show(fragmentManager!!, "addChannel")
        }
    }

    override fun dataChangeListener(list: List<ChannelBean>, position: Int) {
        list.forEach { it.editStatus = 0 }  //重置编辑状态
        update(list)
        if (position != 100000) id_view_Pager.currentItem = position
    }

    private fun update(list: List<ChannelBean>) {
        id_tab_pager_indicator.setDataList(list)
        pagerAdapter.setData(list)
    }

    override fun onChannelItemMoved(list: List<ChannelBean>, function: () -> Unit) {
        model.updateSort(this, list) { function() }
    }
}

class MyViewPager @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val baseFragmentMap = hashMapOf<Int, BaseFragment>()

    private var mDataList = listOf<ChannelBean>()

    fun setData(list: List<ChannelBean>) {
        mDataList = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int = mDataList.size

    override fun getItem(position: Int): Fragment {
        var fragment: BaseFragment? = baseFragmentMap[position]
        if (fragment == null) {
            fragment = NewsFragment.newInstance(mDataList[position].id)
            baseFragmentMap[position] = fragment
        }
        return fragment
    }
}