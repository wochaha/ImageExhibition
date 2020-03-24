package com.example.imageexhibition.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.imageexhibition.R
import kotlinx.android.synthetic.main.common_toolbar.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    //init view
    val mCommonToolbar:androidx.appcompat.widget.Toolbar
        get() = toolbar

    private val mViewModel:BaseViewModel by lazy {
        getViewModel(BaseViewModel::class.java)
    }

    //extension function for toolbar(androidx)
    public fun androidx.appcompat.widget.Toolbar.init(
        title: String,
        @DrawableRes icon: Int = R.drawable.common_ic_back,
        listener: View.OnClickListener? = View.OnClickListener { finish() }
    ) {
        tv_title.text = title
        this.title = ""
        setSupportActionBar(this)
        if (listener == null) {
            navigationIcon = null
        } else {
            setNavigationIcon(icon)
            setNavigationOnClickListener(listener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //use immersive toolbar
        if (Build.VERSION.SDK_INT >= 19) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }else if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    open fun initView(@LayoutRes layoutRes: Int){
    }

    open fun getFactory(): ViewModelProvider.NewInstanceFactory?{
        return null
    }

    open fun <T:BaseViewModel> getViewModel(clazz: Class<T>):T{
        return if (getFactory() != null){
            ViewModelProviders.of(this,getFactory()).get(clazz)
        }else{
            ViewModelProviders.of(this).get(clazz)
        }
    }

    override fun onStart() {
        super.onStart()
        this.mViewModel.register()
    }

    override fun onRestart() {
        super.onRestart()
        this.mViewModel.register()
    }

    override fun onResume() {
        super.onResume()
        this.mViewModel.register()
    }

    override fun onPause() {
        super.onPause()
        this.mViewModel.unRegister()
    }

    override fun onStop() {
        super.onStop()
        this.mViewModel.unRegister()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mViewModel.unRegister()
    }

}