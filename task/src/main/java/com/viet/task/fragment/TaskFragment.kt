package com.viet.task.fragment

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.magicwindow.core.ui.ItemClickSupport
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.task.R
import com.viet.task.adapter.TaskAdapter
import com.viet.task.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_task.*


/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class TaskFragment : RealVisibleHintBaseFragment() {

    private val model: TaskViewModel by viewModelDelegate(TaskViewModel::class)

    private val adapter: TaskAdapter = TaskAdapter()

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_task
    }

    override fun initView(view: View) {
        recycler_view_task.adapter = adapter
        recycler_view_task.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        loadTaskListData()
        loadOtherData()
        initEvent()
    }

    private fun initEvent() {
        refreshLayout.setOnRefreshListener {
            loadTaskListData()
            it.finishRefresh()
        }
        ItemClickSupport.addTo(recycler_view_task).addOnChildClickListener(R.id.btn_right,object :ItemClickSupport.OnChildClickListener{
            override fun onChildClicked(recyclerView: RecyclerView, position: Int, v: View) {
                    toast("等任务接口出来，我就知道要干什么了")
            }
        })
        btn_test.clickWithTrigger {
            val test = TaskViewModel.Test("hahaha", "hello")
            model.live.value = test
        }
    }

    override fun onFragmentFirstVisible() {
        refreshLayout.autoRefresh()
    }

    private fun loadTaskListData() {
        model.getTaskGroupList().observe(this, Observer {
            toast("刷新列表")
            adapter.setData(it?.generateTaskList())
        })
    }
    private fun loadOtherData() {
        model.getOtherData().observe(this, Observer {
            it?.let {
                toast("刷新数据："+it.name)
            }
        })
    }
}
