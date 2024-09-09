package com.example.androidmviharch.mvih.viewlayer.viewintent

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import kotlin.reflect.KClass

/**
 * Função utilitária para construção do [IntentHandlerProvider]
 */
inline fun <S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> intentHandlers(
    builderAction: IntentHandlerProviderBuilder<S, VA, VD, VI>.() -> Unit
): IntentHandlerProvider<S, VA, VD, VI> {
    val builder = IntentHandlerProviderBuilder<S, VA, VD, VI>()
    builder.builderAction()
    return builder.build()
}

/**
 * Builder para construção do [IntentHandlerProvider]
 */
class IntentHandlerProviderBuilder<S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent> @PublishedApi internal constructor() {
    private val intentHandlers: MutableMap<KClass<out ViewIntent>, IntentHandler<S, VA, VD, out VI>> =
        mutableMapOf()

    fun <T : VI> addHandler(clazz: KClass<T>, intentHandler: IntentHandler<S, VA, VD, T>) {
        intentHandlers[clazz] = intentHandler
    }

    @PublishedApi
    internal fun build(): IntentHandlerProvider<S, VA, VD, VI> = IntentHandlerProvider(intentHandlers)
}

/**
 * Adiciona um [IntentHandler] para um [ViewIntent] específico
 */
inline fun <S : State, VA : ViewAction, VD : ViewData, VI : ViewIntent, reified T : VI> IntentHandlerProviderBuilder<S, VA, VD, VI>.addHandler(
    intentHandler: IntentHandler<S, VA, VD, T>
): Unit = addHandler(T::class, intentHandler)
