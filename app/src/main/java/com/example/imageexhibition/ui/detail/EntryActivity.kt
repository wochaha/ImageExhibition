package com.example.imageexhibition.ui.detail

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.imageexhibition.R
import com.example.imageexhibition.base.BaseActivity
import com.example.imageexhibition.base.BaseViewModel

class EntryActivity : BaseActivity() {
    private val title by lazy {
        intent.getStringExtra("title")
    }

    private val url by lazy {
        intent.getStringExtra("url")
    }

    private val fragment:ImageFragment by lazy {
        ImageFragment.newInstance(url ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView(R.layout.activity_entry)

        addFragment(R.id.entry_container,fragment)
    }

    override fun initView(@LayoutRes layoutRes: Int) {
        super.initView(layoutRes)
        setContentView(layoutRes)
        mCommonToolbar.init(title ?: "")
    }
}
