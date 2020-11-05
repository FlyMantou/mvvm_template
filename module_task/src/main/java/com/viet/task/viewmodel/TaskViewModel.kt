package com.viet.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viet.news.core.domain.response.TaskResponse
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class TaskViewModel: BaseViewModel() {
    val live = MutableLiveData<Test>()
    lateinit var test:Test
    init {
        test = Test("huanghai","hello")
        live.value = test
    }

    fun getTaskGroupList(): LiveData<TaskResponse> {
        return FileUtils.handleVirtualData(TaskResponse::class.java)
    }
    fun getOtherData(): LiveData<Test> {
        return live
    }
    data class Test(var name:String,val value:String)
}