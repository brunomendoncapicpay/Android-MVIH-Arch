package com.example.androidmviharch.mvih.viewlayer.viewmodifier

import android.content.Context
import androidx.viewbinding.ViewBinding

interface ViewBindingState<VD : ViewData, VB : ViewBinding> : State {
    fun render(viewData: VD, viewBinding: VB, context: Context)
}
