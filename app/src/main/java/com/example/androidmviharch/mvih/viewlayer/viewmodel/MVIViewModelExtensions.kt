package com.example.androidmviharch.mvih.viewlayer.viewmodel

import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewData
import com.example.androidmviharch.mvih.viewlayer.viewintent.ViewIntent
import com.example.androidmviharch.mvih.viewlayer.viewmodel.MVIViewModel
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.State
import com.example.androidmviharch.mvih.viewlayer.viewmodifier.ViewAction
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ViewModelOwnerDefinition
import org.koin.androidx.viewmodel.dsl.setIsViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.scope.BundleDefinition
import org.koin.core.definition.BeanDefinition
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 * Função de extensão para criação de [MVIViewModel] com Koin
 *
 * @param S Tipo do estado
 * @param VA Tipo da ação
 * @param VD Tipo dos dados da view
 * @param VI Tipo do intent
 * @param qualifier Qualificador do ViewModel - opcional
 * @param definition Definição do ViewModel - opcional
 * @return Definição do ViewModel
 */
inline fun <reified S : State,
    reified VA : ViewAction,
    reified VD : ViewData,
    reified VI : ViewIntent> ScopeDSL.mviViewModel(
        qualifier: Qualifier? = null,
        noinline definition: Scope.(DefinitionParameters) -> MVIViewModel<S, VA, VD, VI>
    ): BeanDefinition<MVIViewModel<S, VA, VD, VI>> {
    val mviQualifier = qualifier
        ?: named("${S::class.simpleName}${VA::class.simpleName}${VD::class.simpleName}${VI::class.simpleName}")
    val beanDefinition = factory(mviQualifier, false, definition)
    beanDefinition.setIsViewModel()
    return beanDefinition
}

/**
 * Função de extensão para obtenção de [MVIViewModel] com Koin
 *
 * @param S Tipo do estado
 * @param VA Tipo da ação
 * @param VD Tipo dos dados da view
 * @param VI Tipo do intent
 * @param qualifier Qualificador do ViewModel - opcional
 * @param state Definição do estado - opcional
 * @param owner Definição do dono do ViewModel - opcional
 * @return Lazy do ViewModel
 */
inline fun <reified S : State, reified VA : ViewAction, reified VD : ViewData, reified VI : ViewIntent> ScopeActivity.mviViewModel(
    qualifier: Qualifier? = null,
    noinline state: BundleDefinition? = null,
    noinline owner: ViewModelOwnerDefinition = { ViewModelOwner.from(this, this) },
): Lazy<MVIViewModel<S, VA, VD, VI>> {
    val mviQualifier = qualifier
        ?: named("${S::class.simpleName}${VA::class.simpleName}${VD::class.simpleName}${VI::class.simpleName}")
    return lazy(LazyThreadSafetyMode.NONE) {
        getViewModel(mviQualifier, state, owner, MVIViewModel::class, null) as MVIViewModel<S, VA, VD, VI>
    }
}
