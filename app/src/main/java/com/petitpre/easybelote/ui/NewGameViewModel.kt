package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import androidx.navigation.NavController
import com.petitpre.easybelote.model.Game
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewGameViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    val maxScore = MutableLiveData<String>(1001.toString())
    val advert = MutableLiveData<Boolean>(false)
    val players: List<MutableLiveData<String>> = (0..3).map { MutableLiveData<String>(null) }

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun startGame(controller: NavController) {
        viewModelScope.launch {
            val game = Game(
                max_score = maxScore.value?.toInt() ?: 1001,
                advert = advert.value ?: false
            )

            game.player_one = players[0].value.orEmpty()
            game.player_two = players[1].value.orEmpty()
            game.player_three = players[2].value.orEmpty()
            game.player_four = players[3].value.orEmpty()

            val id = gameRepository.saveGame(game)

            val direction = NewGameFragmentDirections.actionNewgameFragmentToPlayingFragment(id)
            controller.navigate(direction)
        }
    }
}

class NewGameViewModelFactory(
    private val gameRepository: GameRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewGameViewModel(gameRepository) as T
    }
}