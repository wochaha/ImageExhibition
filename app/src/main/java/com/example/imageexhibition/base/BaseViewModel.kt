package com.example.imageexhibition.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    open fun cancel(){}

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

    open fun update(){}
}