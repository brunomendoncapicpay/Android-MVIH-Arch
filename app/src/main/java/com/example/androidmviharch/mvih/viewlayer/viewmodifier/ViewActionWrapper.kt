package com.example.androidmviharch.mvih.viewlayer.viewmodifier

import com.example.androidmviharch.mvih.viewlayer.viewmodel.MVIErrorHandler

/**
 * Classe responsável por encapsular propriedades publicadas nas actions.
 * Propriedade [MVIErrorHandler] de uso interno da arquitetura MVI
 */
data class ViewActionWrapper<VA : ViewAction> internal constructor(
    val action: VA,
    val errorHandler: MVIErrorHandler
)
