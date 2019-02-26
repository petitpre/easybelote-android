package com.petitpre.easybelote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
    gameId: Long
) : AbstractGameViewModel(gameRepository, gameId) {

    val bidder: LiveData<Int>

    val myScore: LiveData<Long>
    val otherScore: LiveData<Long>

    init {
        bidder = Transformations.map(gameWithRound) { it.game.bidder }
        myScore = Transformations.map(gameWithRound) { it.getMyScore() }
        otherScore = Transformations.map(gameWithRound) { it.getOtherScore() }
    }

    fun takeNext() = viewModelScope.launch {
        gameWithRound.value?.let { gameWithRound ->
            gameWithRound.game.bidder = (gameWithRound.game.bidder + 1) % 4
            gameRepository.saveGame(gameWithRound.game)
        }
    }
}
