package com.petitpre.easybelote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
    gameId: Long
) : AbstractGameViewModel(gameRepository, gameId) {

    val dealer: LiveData<Int>

    val myScore: LiveData<Long>
    val otherScore: LiveData<Long>

    init {
        dealer = Transformations.map(gameWithRound) { it.game.dealer }
        myScore = Transformations.map(gameWithRound) { it.getMyScore() }
        otherScore = Transformations.map(gameWithRound) { it.getOtherScore() }
    }

    fun takeNext() = viewModelScope.launch {
        gameWithRound.value?.let { gameWithRound ->
            gameWithRound.game.dealer = (gameWithRound.game.dealer + 1) % 4
            gameRepository.saveGame(gameWithRound.game)
        }
    }
}
