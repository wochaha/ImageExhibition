package com.example.imageexhibition.ui.exhibition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExhibitionFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExhibitionViewModel() as T
    }
}