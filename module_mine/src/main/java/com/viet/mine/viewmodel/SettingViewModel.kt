package com.viet.mine.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.viet.mine.repository.MineRepository
import com.viet.news.core.viewmodel.BaseViewModel

class SettingViewModel(var repository: MineRepository = MineRepository()) : BaseViewModel() {
    var feedback: MutableLiveData<String> = MutableLiveData()    //提交反馈内容
    var submitEnable: MutableLiveData<Boolean> = MutableLiveData()   //提交按钮是否可用
    var count: MutableLiveData<Int> = MutableLiveData()   //剩余字数

    fun checkFeedBackSubmitBtnEnable() {
        submitEnable.value = feedback.value != null && feedback.value!!.isNotEmpty()
    }


    fun feedBackSubmitEnable(): Boolean = when {
        feedback.value == null || feedback.value.isNullOrBlank() -> {
            false
        }
        else -> true
    }

    //反馈
    fun feedBack(owner: LifecycleOwner, feedback: String, finish: (isSuccess: Boolean) -> Unit) {
        return repository.feedBack(feedback).observe(owner, Observer { resource ->
            resource?.work(
                    onSuccess = {
                        finish(true)
                    },
                    onError = {
                        finish(false)
                    }
            )
        })
    }
}