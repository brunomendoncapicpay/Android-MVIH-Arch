package com.example.androidmviharch.mvih.viewlayer.guard

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewModifier
import kotlin.reflect.KClass

/**
 * Função de extensão para facilitar a criação de [StateGuard]
 * @param builderAction Ação de configuração do [StateGuardBuilder]
 * @return [StateGuard]
 */
inline fun stateGuard(
    builderAction: StateGuardBuilder.() -> Unit
): StateGuard {
    val builder = StateGuardBuilder()
    builder.builderAction()
    return builder.build()
}

/**
 * Builder para facilitar a criação de [StateGuard]
 */
class StateGuardBuilder {
    private val statesAllowed: MutableMap<KClass<out State>, MutableSet<KClass<out ViewModifier>>> = mutableMapOf()

    fun <S : State, VM : ViewModifier> allowTransition(transaction: Pair<KClass<S>, KClass<VM>>) {
        allowTransition(transaction.first, setOf(transaction.second))
    }

    fun <S : State, VM : ViewModifier> allowTransitions(transactions: Pair<KClass<S>, Collection<KClass<out VM>>>) {
        allowTransition(transactions.first, transactions.second.toSet())
    }

    @PublishedApi
    internal fun build(): StateGuard = StateGuardImpl(statesAllowed)

    private fun <S : State, VM : ViewModifier> allowTransition(from: KClass<S>, to: Set<KClass<out VM>>) {
        if (statesAllowed[from] != null) {
            statesAllowed[from]!!.addAll(to)
        } else {
            statesAllowed[from] = to.toMutableSet()
        }
    }
}
