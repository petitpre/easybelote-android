package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.*

open class PlayingViewModel(
    private val gameRepository: GameRepository,
    gameId: Long
) : GameViewModel(gameRepository, gameId) {

    fun takeNext() = viewModelScope.launch {
        game.value?.let { game ->
            game.game.bidder = (game.game.bidder + 1) % 4
            gameRepository.saveGame(game.game)
        }
    }
}
