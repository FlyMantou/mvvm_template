package com.viet.news.core.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.viet.news.core.api.ApiEmptyResponse
import com.viet.news.core.api.ApiErrorResponse
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.ApiSuccessResponse
import com.viet.news.core.ext.isBlank
import com.viet.news.core.vo.Resource
import com.viet.news.core.vo.Status
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    protected val liveData: MutableLiveData<RequestType> = MutableLiveData()

    init {
        result.value = Resource(Status.LOADING)
        result.value?.setOnCancelListener { cancel() }
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        if (dbSource.hasActiveObservers().not()) {
            fetchFromNetwork(dbSource)
        } else {
            result.addSource(dbSource) { resultType ->
                result.removeSource(dbSource)
                if (shouldFetch(resultType)) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(dbSource) { result.value = result.value?.success(it) }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private lateinit var apiResponse: LiveData<ApiResponse<RequestType>>
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        apiResponse = createCall()
        result.addSource(dbSource) { result.value?.loading()?.let { setValue(it) } }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    if (isBlank(response.body)) {
                        result.value = result.value?.success()
                        return@addSource
                    }
                    processResponse(response)?.let { Observable.fromCallable { saveCallResult(it) }.subscribeOn(Schedulers.io()).subscribe() }
                    result.addSource(loadFromDb()) { resultType -> result.value = result.value?.success(resultType) }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    if (dbSource.hasActiveObservers()) {
                        result.addSource(dbSource) { it -> result.value = result.value?.error(it, response.errorMessage) }
                    } else {
                        result.value = result.value?.error(message = response.errorMessage)
                    }
                }
                is ApiEmptyResponse -> result.addSource(loadFromDb()) { newData -> result.value = result.value?.success(newData) }
            }
        }
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    // 将网络获取的数据存储到db
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // 是否需要从网络重新获取
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // 从db内获取cache数据
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // 从网络中获取
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // 网络获取失败
    @MainThread
    protected open fun onFetchFailed() {
    }

    @MainThread
    private fun cancel() {
        result.removeSource(apiResponse)
    }

}