package com.example.imageexhibition.ui.exhibition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imageexhibition.base.BaseRepository
import com.example.imageexhibition.model.ImageAndTextModel
import com.example.imageexhibition.util.ApiServer
import com.example.imageexhibition.util.ApiService
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class ExhibitionRepository:BaseRepository() {
    private var mPage:Int = 0
    val mErrorMessage = MutableLiveData<String>()
    val mNewReceiveNum = MutableLiveData<Int>()
    val mInformationList = MutableLiveData<MutableList<ImageAndTextModel>>()
    val mRefreshStatus = MutableLiveData<Boolean>()
    val mUpdateStatus = MutableLiveData<Boolean>()

    init {
        mErrorMessage.value = ""
        mNewReceiveNum.value = 0
        mInformationList.value = mutableListOf()
        mRefreshStatus.value = true
        mUpdateStatus.value = false
    }

    override fun update(){
        GlobalScope.launch(Dispatchers.Main) {
            mRefreshStatus.value = true
            try {
                val result:MutableList<ImageAndTextModel> = updateContent()

                mNewReceiveNum.value = result.size
                Log.d("ExhibitionRepository","拉取到${mNewReceiveNum.value}条数据~")

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

    override fun cancel(){
        GlobalScope.cancel()
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