package com.example.imageexhibition.ui.exhibition

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.imageexhibition.base.BaseViewModel
import com.example.imageexhibition.model.ImageAndTextModel

/**
 * LiveData触发onChanged回调方法的时机为value内的对象发生改变,若只是对象的值发生改变是无法触发的
 *
 * ViewModel应该只有对数据的引用以及少量的更新数据的逻辑
 */
class ExhibitionViewModel : BaseViewModel() {
    private val repository = ExhibitionRepository()
    val mInformationList = MutableLiveData<MutableList<ImageAndTextModel>>()
    val mUpdateStatus = MutableLiveData<Boolean>()
    val mNewReceiveNum = MutableLiveData<Int>()
    val mErrorMessage = MutableLiveData<String>()
    val mRefreshStatus = MutableLiveData<Boolean>()

    private val mInformationListObserver = Observer<MutableList<ImageAndTextModel>> { list->
        mInformationList.value = list
    }
    private val mUpdateStatusObserver = Observer<Boolean> { status->
        mUpdateStatus.value = status
    }
    private val mErrorMessageObserver = Observer<String>{ msg->
        mErrorMessage.value = msg
    }
    private val mNewReceiveObserver = Observer<Int> { num->
        mNewReceiveNum.value = num
    }
    private val mRefreshStatusObserver = Observer<Boolean> { status->
        mRefreshStatus.value = status
    }

    init {
        mInformationList.value = mutableListOf()
        mUpdateStatus.value = false
        mErrorMessage.value = ""
        mNewReceiveNum.value = 0
        mRefreshStatus.value = false
    }

    @MainThread
    override fun register(){
        repository.mInformationList.removeObserver(mInformationListObserver)
        repository.mRefreshStatus.removeObserver(mRefreshStatusObserver)
        repository.mErrorMessage.removeObserver(mErrorMessageObserver)
        repository.mNewReceiveInformationNum.removeObserver(mNewReceiveObserver)
        repository.mUpdateStatus.removeObserver(mUpdateStatusObserver)

        repository.mInformationList.observeForever(mInformationListObserver)
        repository.mRefreshStatus.observeForever(mRefreshStatusObserver)
        repository.mErrorMessage.observeForever(mErrorMessageObserver)
        repository.mNewReceiveInformationNum.observeForever(mNewReceiveObserver)
        repository.mUpdateStatus.observeForever(mUpdateStatusObserver)
    }

    @MainThread
    override fun unRegister(){
        repository.mInformationList.removeObserver(mInformationListObserver)
        repository.mRefreshStatus.removeObserver(mRefreshStatusObserver)
        repository.mErrorMessage.removeObserver(mErrorMessageObserver)
        repository.mNewReceiveInformationNum.removeObserver(mNewReceiveObserver)
        repository.mUpdateStatus.removeObserver(mUpdateStatusObserver)
    }

    fun getInfoFromNetwork() = repository.update()
}