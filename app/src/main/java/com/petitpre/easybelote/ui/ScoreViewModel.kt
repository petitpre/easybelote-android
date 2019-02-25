package com.petitpre.easybelote.ui

import androidx.lifecycle.*
import androidx.navigation.NavController
import com.petitpre.easybelote.model.Declaration
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.Round
import com.petitpre.easybelote.model.TeamScore
import kotlinx.coroutines.*

class ScoreViewModel(
    val gameRepository: GameRepository,
    gameId: Long
) : GameViewModel(gameRepository, gameId) {

    val newround = MutableLiveData<Round>(Round(gameId = gameId, team1 = TeamScore(), team2 = TeamScore()))

    val myScoreRound: LiveData<String>
    val otherScoreRound: LiveData<String>

    init {
        myScoreRound = Transformations.map(newround, { it.team1.score.toString() })
        otherScoreRound = Transformations.map(newround, { it.team2.score.toString() })
    }

    fun addDeclaration(myTeam: Boolean, declaration: Declaration) {
        val team = if (myTeam) newround.value!!.team1 else newround.value!!.team2
        val otherTeam = if (myTeam) newround.value!!.team2 else newround.value!!.team1

        newround.value?.let { round ->

            when (declaration) {
                Declaration.belote -> {
                    team.score = 252
                    otherTeam.score = 0
                }
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
                    team1 = TeamScore(myScoreRound.value?.toLong() ?: 0L),
                    team2 = TeamScore(otherScoreRound.value?.toLong() ?: 0L)
                )
                gameRepository.addRound(round)
            }
            controller.navigateUp()
        }
    }
}
