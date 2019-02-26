package com.petitpre.easybelote.ui

import com.petitpre.easybelote.model.GameRepository

class HistoryViewModel(
    val gameRepository: GameRepository,
    gameId: Long
) : AbstractGameViewModel(gameRepository, gameId) {

}
