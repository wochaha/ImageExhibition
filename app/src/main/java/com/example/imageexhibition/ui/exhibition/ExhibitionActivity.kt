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
        initView(R.layout.activity_main)
        observer()

        //update data
        mViewModel.update()
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
                mViewModel.update()
            }
        }
    }

    private fun observer(){
        //可以考虑抽到adapter中使用AdapterDataObservable监听该LiveData
        mViewModel.mInformationList.observe(this, Observer { list ->
            mExhibitionAdapter.exhibitions.clear()
            mExhibitionAdapter.exhibitions.addAll(list)

            mExhibitionAdapter.notifyDataSetChanged()
        })
        mViewModel.mUpdateStatus.observe(this, Observer { result->
            toast(result)
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

    companion object{
        private const val STAG = "ExhibitionActivity"
    }
}
