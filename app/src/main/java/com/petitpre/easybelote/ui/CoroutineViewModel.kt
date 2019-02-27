package com.petitpre.easybelote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.GameWithRound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking


abstract class CoroutineViewModel : ViewModel() {

   private val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


class ViewModelFactory<T : ViewModel>(
    val builder: () -> T
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return builder() as T
    }
}

abstract class AbstractGameViewModel(
    private val gameRepository: GameRepository,
    private val gameId: Long
) : CoroutineViewModel() {

    val gameWithRound: LiveData<GameWithRound>

    init {
        gameWithRound = runBlocking {
            gameRepository.getFullGame(gameId)
        }
    }
}