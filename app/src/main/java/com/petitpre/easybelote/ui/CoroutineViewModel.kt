package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import com.petitpre.easybelote.model.Game
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.GameWithRound
import kotlinx.coroutines.*


abstract class CoroutineViewModel(
) : ViewModel() {

    val viewModelJob = Job()
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

abstract class GameViewModel(
    private val gameRepository: GameRepository,
    private val gameId: Long
) : CoroutineViewModel() {

    val game: LiveData<GameWithRound>
    val players: Array<LiveData<String>>

    val bidder: LiveData<Int>
    val myScore: LiveData<Long>
    val otherScore: LiveData<Long>


    init {
        game = runBlocking {
            gameRepository.getGame(gameId)
        }
        players = arrayOf(
            Transformations.map(game, { it.game.player_one ?: "" }),
            Transformations.map(game, { it.game.player_two ?: "" }),
            Transformations.map(game, { it.game.player_three ?: "" }),
            Transformations.map(game, { it.game.player_four ?: "" })
        )

        bidder = Transformations.map(game, { it.game.bidder })

        myScore = Transformations.map(game, { it.getMyScore() })
        otherScore = Transformations.map(game, { it.getOtherScore() })
    }
}