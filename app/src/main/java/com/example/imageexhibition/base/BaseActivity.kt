package com.example.imageexhibition.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.imageexhibition.R
import kotlinx.android.synthetic.main.common_toolbar.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    //init view
    val mCommonToolbar:androidx.appcompat.widget.Toolbar
        get() = toolbar

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

    fun addFragment(@IdRes layoutIdRes: Int, fragment: Fragment){
        Log.d(this::class.java.simpleName,"add fragment")
        this.supportFragmentManager
            .beginTransaction()
            .add(layoutIdRes,fragment)
            .commitAllowingStateLoss()
    }
}