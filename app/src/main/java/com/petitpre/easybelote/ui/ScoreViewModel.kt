package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import androidx.navigation.NavController
import com.petitpre.easybelote.model.Declaration
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.Round
import kotlinx.coroutines.*

class ScoreViewModel(
    val gameRepository: GameRepository,
    gameId: Long
) : GameViewModel(gameRepository, gameId) {

    val newround = MutableLiveData<Round>(Round(gameId = gameId, myScore = 0, otherScore = 0))

    val myScoreRound: LiveData<String>
    val otherScoreRound: LiveData<String>

    init {
        myScoreRound = Transformations.map(newround, { it.myScore.toString() })
        otherScoreRound = Transformations.map(newround, { it.myScore.toString() })
    }

    fun addDeclaration(myTeam: Boolean, declaration: Declaration) {
        newround.value?.let { round ->

            when (declaration) {
                Declaration.carre_of_aces -> {
                    round.myScore = 252
                    round.otherScore = 0
                }
            }

            if (myTeam) {
                round.myDeclarations = listOf(declaration)
                round.otherDeclarations = emptyList()
            } else {
                round.myDeclarations = emptyList()
                round.otherDeclarations = listOf(declaration)
            }

            newround.value = round
        }
    }

    private fun getScore(takerScore: Int): Pair<Int, Int> {
        if (takerScore > 81) {
            return Pair<Int, Int>(takerScore, 162 - takerScore)
        } else if (takerScore == 81) {
            return Pair<Int, Int>(0, 81)
        } else {
            return Pair<Int, Int>(0, 162)
        }
    }

    fun updateScores(controller: NavController) {
        viewModelScope.launch {
            game.value?.let { game ->
                val round = Round(
                    gameId = game.game.id,
                    myScore = myScoreRound.value?.toLong() ?: 0L,
                    otherScore = otherScoreRound.value?.toLong() ?: 0L
                )

                gameRepository.addRound(round)
            }
            controller.navigateUp()
        }
    }
}
