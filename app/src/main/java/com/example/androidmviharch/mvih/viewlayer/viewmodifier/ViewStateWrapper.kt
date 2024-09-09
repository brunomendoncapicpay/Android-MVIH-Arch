package com.example.androidmviharch.mvih.viewlayer.viewmodifier

import com.example.androidmviharch.mvih.viewlayer.viewmodel.MVIErrorHandler

/**
 * Classe responsável por encapsular propriedades publicadas nos states.
 * Propriedade [MVIErrorHandler] de uso interno da arquitetura MVI
 */
data class ViewStateWrapper<S : State, VD : ViewData> internal constructor(
    val state: S,
    val viewData: VD,
    val errorHandler: MVIErrorHandler
)
