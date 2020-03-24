package com.example.imageexhibition.ui.exhibition

import androidx.lifecycle.MutableLiveData
import com.example.imageexhibition.model.ImageAndTextModel
import com.example.imageexhibition.util.ApiServer
import com.example.imageexhibition.util.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExhibitionRepository {
    private var mPage:Int = 0
    val mErrorMessage = MutableLiveData<String>()
    val mNewReceiveInformationNum = MutableLiveData<Int>()
    val mInformationList = MutableLiveData<MutableList<ImageAndTextModel>>()
    val mRefreshStatus = MutableLiveData<Boolean>()
    val mUpdateStatus = MutableLiveData<Boolean>()

    init {
        mErrorMessage.value = ""
        mNewReceiveInformationNum.value = 0
        mInformationList.value = mutableListOf()
        mRefreshStatus.value = true
        mUpdateStatus.value = false
    }

    fun update(){
        GlobalScope.launch(Dispatchers.Main) {
            mRefreshStatus.value = true
            try {
                val result = updateContent()
                mNewReceiveInformationNum.value = result.size

                result.addAll(mInformationList.value!!)
                mInformationList.value = result

                mUpdateStatus.value = true
                mPage += 1
            }catch (e:Throwable){
                mUpdateStatus.value = false
                mErrorMessage.value = e.message
            }finally {
                mRefreshStatus.value = false
            }
        }
    }

    private suspend fun updateContent():MutableList<ImageAndTextModel>{
        return withContext(Dispatchers.IO){
            ApiServer
                .getApiService(ApiService::class.java)
                .getInfo(mapOf(
                    "type" to "0",
                    "page" to mPage.toString()
                )).data
        }
    }
}