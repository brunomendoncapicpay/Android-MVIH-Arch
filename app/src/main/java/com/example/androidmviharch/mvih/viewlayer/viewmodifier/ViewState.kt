package com.example.androidmviharch.mvih.viewlayer.viewmodifier

import android.content.Context
import android.view.View

interface ViewState<VD : ViewData> : State {
    fun render(viewData: VD, view: View, context: Context)
}
