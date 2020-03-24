package com.example.imageexhibition.ui.exhibition

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.imageexhibition.R
import com.example.imageexhibition.base.BaseActivity
import com.example.imageexhibition.ui.adapter.ExhibitionAdapter
import com.example.imageexhibition.util.getScreenWidth
import kotlinx.android.synthetic.main.activity_main.*

/**
 * bug : 无法显示内容,
 */
class ExhibitionActivity : BaseActivity() {
    private val mRecyclerView:RecyclerView
        get() = exhibition_list
    private val mSwipeRefreshLayout: SwipeRefreshLayout
        get() = exhibition_srl

    //init adapter
    private val mExhibitionAdapter : ExhibitionAdapter by lazy {
        Log.d(STAG,"init adapter...")
        ExhibitionAdapter(
            width = (getScreenWidth()/2.2).toInt()
        )
    }

    //init ViewModel
    private val mViewModel:ExhibitionViewModel by lazy {
        Log.d(STAG,"init ViewModel...")
        getViewModel(ExhibitionViewModel::class.java)
    }

    override fun getFactory(): ViewModelProvider.NewInstanceFactory? {
        return ExhibitionFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.register()
        initView(R.layout.activity_main)
        observer()

        //update data
        mViewModel.getInfoFromNetwork()
    }

    override fun initView(layoutRes: Int) {
        super.initView(layoutRes)
        setContentView(layoutRes)
        mCommonToolbar.init("图片展示",listener = null)

        //configure recycler view
        val gridLayoutManager = GridLayoutManager(this,2)
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.adapter = mExhibitionAdapter

        //configure swipe refresh layout
        mSwipeRefreshLayout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.black)
            )
            setOnRefreshListener {
                mViewModel.getInfoFromNetwork()
            }
        }
    }

    private fun observer(){
        //可以考虑抽到adapter中使用AdapterDataObservable监听该LiveData
        mViewModel.mInformationList.observe(this, Observer { list ->
            Log.d(STAG,"data count: ${list.size}")
            mExhibitionAdapter.exhibitions.clear()
            mExhibitionAdapter.exhibitions.addAll(list)
            Log.d(STAG,"adapter data count: ${mExhibitionAdapter.itemCount}")
            mExhibitionAdapter.notifyDataSetChanged()
        })
        mViewModel.mUpdateStatus.observe(this, Observer { status->
            toast(status)
        })
        mViewModel.mRefreshStatus.observe(this, Observer { status->
            mSwipeRefreshLayout.isRefreshing = status
        })
    }

    private fun toast(status:Boolean) {
        var msg = ""
        if (status){
            val receive = mViewModel.mNewReceiveNum.value
            if (receive != null){
                msg = if (receive == 0){
                    "暂未获取到新的数据~"
                }else{
                    "获取到${receive}条新的数据"
                }
            }
        }else{
            msg = mViewModel.mErrorMessage.value.toString()
        }

        if (msg.isNotEmpty() && !msg.isBlank()){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        }
    }

    //最好是可以抽象一个ViewModel基类和Activity基类
    //在基类完成创建一个方法实现通过传入的factory和ViewModel.class获取对应的ViewModel
    //实现在生命周期内对ViewModel的注册和解绑
    override fun onStart() {
        super.onStart()
        mViewModel.register()
    }

    override fun onRestart() {
        super.onRestart()
        mViewModel.register()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.register()
    }

    override fun onPause() {
        super.onPause()
        mViewModel.unRegister()
    }

    override fun onStop() {
        super.onStop()
        mViewModel.unRegister()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.unRegister()
    }

    companion object{
        private const val STAG = "ExhibitionActivity"
    }
}
