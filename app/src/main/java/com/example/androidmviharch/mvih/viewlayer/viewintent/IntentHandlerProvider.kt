@file:Suppress("MaximumLineLength")

package com.example.androidmviharch.mvih.viewlayer.viewintent

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import kotlin.reflect.KClass

/**
 * Classe responsável por prover os [IntentHandler]s para cada [ViewIntent] da View
 */
class IntentHandlerProvider<S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> internal constructor(
    private val intentHandlers: Map<KClass<out ViewIntent>, IntentHandler<S, VA, VD, out VI>>
) {
    @Suppress("SuppressionViolationDetector", "CastOperatorDetector", "UNCHECKED_CAST")
    fun <T : VI> get(clazz: KClass<out T>): IntentHandler<S, VA, VD, T>? =
        intentHandlers[clazz] as? IntentHandler<S, VA, VD, T>
}
