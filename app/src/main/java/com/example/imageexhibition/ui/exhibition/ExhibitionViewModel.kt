package com.example.imageexhibition.ui.exhibition

import android.util.Log
import androidx.lifecycle.*
import com.example.imageexhibition.base.BaseViewModel
import com.example.imageexhibition.model.ImageAndTextModel


class ExhibitionViewModel : BaseViewModel() {
    private val repository:ExhibitionRepository = ExhibitionRepository()

    val mInformationList = MutableLiveData<MutableList<ImageAndTextModel>>()
    val mUpdateStatus = MutableLiveData<Boolean>()
    val mNewReceiveNum = MutableLiveData<Int>()
    val mErrorMessage = MutableLiveData<String>()
    val mRefreshStatus = MutableLiveData<Boolean>()

    private val mInformationListObserver = Observer<MutableList<ImageAndTextModel>> { list->
        Log.d("ExhibitionViewModel",list.toString())
        mInformationList.value = list
    }
    private val mUpdateStatusObserver = Observer<Boolean> { result->
        mUpdateStatus.value = result
    }
    private val mErrorMessageObserver = Observer<String>{ msg->
        Log.d("ExhibitionViewModel","error msg: $msg")
        mErrorMessage.value = msg
    }
    private val mNewReceiveObserver = Observer<Int> { num->
        Log.d("ExhibitionViewModel","拉取到${num}条数据~")
        mNewReceiveNum.value = num
    }
    private val mRefreshStatusObserver = Observer<Boolean> { status->
        Log.d("ExhibitionViewModel","update result: $status")
        mRefreshStatus.value = status
    }

    init {
        mInformationList.value = mutableListOf()
        mUpdateStatus.value = false
        mErrorMessage.value = ""
        mNewReceiveNum.value = 0
        mRefreshStatus.value = false

        repository.mInformationList.observeForever(mInformationListObserver)
        repository.mUpdateStatus.observeForever(mUpdateStatusObserver)
        repository.mRefreshStatus.observeForever(mRefreshStatusObserver)
        repository.mErrorMessage.observeForever(mErrorMessageObserver)
        repository.mNewReceiveNum.observeForever(mNewReceiveObserver)
    }

    override fun cancel() {
        super.cancel()
        repository.mInformationList.removeObserver(mInformationListObserver)
        repository.mUpdateStatus.removeObserver(mUpdateStatusObserver)
        repository.mRefreshStatus.removeObserver(mRefreshStatusObserver)
        repository.mErrorMessage.removeObserver(mErrorMessageObserver)
        repository.mNewReceiveNum.removeObserver(mNewReceiveObserver)
        repository.cancel()
    }

    override fun update() = repository.update()
}