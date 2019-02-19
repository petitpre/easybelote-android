package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import com.petitpre.easybelote.model.Game
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.*

class PlayingViewModel(
    private val gameRepository: GameRepository,
    private val gameId: Long
) : ViewModel() {

    val game: LiveData<Game>
    val players: Array<LiveData<String>>

    val taking = MutableLiveData<Long>(0L)
    val myScore = MutableLiveData<Long>(0L)
    val otherScore = MutableLiveData<Long>(0L)

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        game = runBlocking {
            gameRepository.getGame(gameId)
        }
        players = arrayOf(
            Transformations.map(game, { it?.player_one ?: "" }),
            Transformations.map(game, { it?.player_two ?: "" }),
            Transformations.map(game, { it?.player_three ?: "" }),
            Transformations.map(game, { it?.player_four ?: "" })
        )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun takeNext() = viewModelScope.launch {
        taking.value = ((taking.value ?: 0) + 1) % 4
    }

}

class PlayingViewModelFactory(
    private val gameRepository: GameRepository,
    private val gameId: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayingViewModel(gameRepository, gameId) as T
    }
}