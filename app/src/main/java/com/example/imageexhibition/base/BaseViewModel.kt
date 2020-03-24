package com.example.imageexhibition.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    open fun register(){}

    open fun unRegister(){}
}