package com.petitpre.easybelote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.petitpre.easybelote.model.Declaration
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.Round
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class GameRoundViewModel(
    val gameRepository: GameRepository,
    val gameId: Long,
    private val roundId: Long
) : AbstractGameViewModel(gameRepository, gameId) {

    val round: MutableLiveData<Round>
    val bidder: LiveData<Int>
    val team1Score: LiveData<String>
    val team2Score: LiveData<String>

    init {
        round = MutableLiveData(runBlocking {
            gameRepository.getRound(roundId)
                ?: Round(
                    gameId = gameId,
                    bidder = gameWithRound.value?.game?.bidder ?: 0
                )
        })

        bidder = Transformations.map(round) { it.bidder }
        team1Score = Transformations.map(round) { it.team1.score.toString() }
        team2Score = Transformations.map(round) { it.team2.score.toString() }
    }

    fun setTeamScore(team1: Boolean, scoreStr: String) {
        try {
            round.value?.let {
                val scoreLong = scoreStr.toLong()
                if (team1) {
                    it.team1.score = scoreLong
                } else {
                    it.team2.score = scoreLong
                }
            }
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    fun removeDeclaration(myTeam: Boolean, declaration: Declaration) {
        round.update { r ->
            val team = if (myTeam) r.team1 else r.team2
            team.declarations.remove(declaration)
        }
    }

    fun addDeclaration(myTeam: Boolean, declaration: Declaration) {
        round.update { r ->
            val team = if (myTeam) r.team1 else r.team2
            val otherTeam = if (myTeam) r.team2 else r.team1

            when (declaration) {
                Declaration.Capot -> {
                    team.score = 252
                    otherTeam.score = 0
                    team.declarations.add(Declaration.Capot)
                    otherTeam.declarations.remove(Declaration.Capot)
                }
                Declaration.Belote -> {
                    team.declarations.add(Declaration.Belote)
                    otherTeam.declarations.remove(Declaration.Belote)
                }
                else -> {
                    team.declarations.add(declaration)
                    otherTeam.declarations.removeAll(
                        listOf(
                            Declaration.Tierce,
                            Declaration.Quarte,
                            Declaration.Quinte,
                            Declaration.Square,
                            Declaration.CarreOfNine,
                            Declaration.CarreOfJacks
                        )
                    )
                }
            }
        }
    }

    fun setBidder(bidder: Int) = viewModelScope.launch {
        round.update { r ->
            r.bidder = bidder
        }
    }

    fun updateScores(donelistener: () -> Unit) = viewModelScope.launch {
        round.value?.let { round ->
            gameRepository.addRound(round)
        }
        donelistener()
    }
}

private fun <T> MutableLiveData<T>.update(update: (T) -> Unit) {
    this.value?.let { r ->
        update(r)
        this.value = r
    }
}
