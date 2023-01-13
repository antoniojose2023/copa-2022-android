package me.dio.copa.catar.features

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.usecase.UseCaseDisableNotificationFor
import me.dio.copa.catar.domain.usecase.UseCaseEnableNotificationFor
import me.dio.copa.catar.domain.usecase.UseCaseGetMatches
import me.dio.copa.catar.remote.UnexpectedException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetMatches: UseCaseGetMatches,
    private val useCaseDisableNotificationFor: UseCaseDisableNotificationFor,
    private val useCaseEnableNotificationFor: UseCaseEnableNotificationFor,
): BaseViewModel<MainUiState, MainUiAction>(MainUiState()){

       init {
           fecthMatches()
       }

       private fun fecthMatches() = viewModelScope.launch {
                useCaseGetMatches()
                    .flowOn(Dispatchers.Main)
                    .catch {
                         when(it){
                             is NotFoundException -> {
                               sendAction(MainUiAction.MatchesNotFound(it.message ?: "Erro"))
                             }
                             is UnexpectedException ->{
                                 sendAction(MainUiAction.Unexpected)
                             }
                         }
                    }
                    .collect{ matches ->
                        setState {
                            copy(matches = matches)
                        }
                    }
       }


      fun toogleState(matches: MatchDomain){
           viewModelScope.launch {
               runCatching {
                   withContext(Dispatchers.Main){
                        val action = if(matches.notificationEnabled){
                            useCaseDisableNotificationFor(matches.id)
                            MainUiAction.ActionNotificationDisable(matches)
                        }else{
                            useCaseEnableNotificationFor(matches.id)
                            MainUiAction.ActionNotificationEnabled(matches)
                        }

                       sendAction( action )
                   }
               }
           }
      }

}

data class MainUiState(
    val matches: List<MatchDomain> = emptyList()){
}

sealed class MainUiAction{
    object Unexpected: MainUiAction()
    data class MatchesNotFound(var mensage: String ): MainUiAction()
    data class ActionNotificationEnabled(val matches: MatchDomain): MainUiAction()
    data class ActionNotificationDisable(val matches: MatchDomain): MainUiAction()
}

