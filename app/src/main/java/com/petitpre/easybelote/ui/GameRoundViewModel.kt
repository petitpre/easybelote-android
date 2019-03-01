package com.petitpre.easybelote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.petitpre.easybelote.model.Declaration
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.Round
import com.petitpre.easybelote.model.TeamScore
import com.petitpre.easybelote.utils.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class GameRoundViewModel(
    val gameRepository: GameRepository,
    val gameId: Long,
    val roundId: Long,
    val bidding: Int
) : AbstractGameViewModel(gameRepository, gameId) {

    val round: MutableLiveData<Round>
    val team1Score: LiveData<String>
    val team2Score: LiveData<String>

    val team1Declaration: List<LiveData<AvailableDeclaration>>
    val team2Declaration: List<LiveData<AvailableDeclaration>>

    init {
        val game = runBlocking { gameRepository.getGame(gameId) }
        round = MutableLiveData(runBlocking {
            gameRepository.getRound(roundId) ?: Round(
                gameId = gameId, team1 = TeamScore(
                    score = 0, bidding = bidding == 0 || bidding == 2
                ),
                team2 = TeamScore(
                    score = 0, bidding = bidding == 1 || bidding == 3
                )
            )
        })

        team1Score = Transformations.map(round) { it.team1.score.toString() }
        team2Score = Transformations.map(round) { it.team2.score.toString() }

        val availableDeclaration =
            if (game.declarations) Declaration.values() else arrayOf(Declaration.Capot, Declaration.Belote)

        team1Declaration = availableDeclaration
            .map { declaration ->
                Transformations.map(round) {
                    AvailableDeclaration(
                        declaration,
                        it.team1.declarations.contains(declaration)
                    )
                }
            }

        team2Declaration = availableDeclaration
            .map { declaration ->
                Transformations.map(round) {
                    AvailableDeclaration(
                        declaration,
                        it.team2.declarations.contains(declaration)
                    )
                }
            }
    }

    fun setTeamScore(team1: Boolean, scoreStr: String) {
        try {
            round.update { round ->
                val biddingScore = if (team1 && round.team1.bidding) scoreStr.toLong() else 162 - scoreStr.toLong()
                val biddingTeam = if (round.team1.bidding) round.team1 else round.team2
                val otherTeam = if (round.team1.bidding) round.team2 else round.team1

                if (biddingScore > 81) {
                    biddingTeam.score = biddingScore
                    otherTeam.score = 162 - biddingScore
                } else {
                    biddingTeam.score = 0
                    otherTeam.score = 162
                }

                biddingTeam.declarations.remove(Declaration.Capot)
                otherTeam.declarations.remove(Declaration.Capot)
            }
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    fun setDeclaration(myTeam: Boolean, declaration: Declaration, enabled: Boolean) {
        round.update { r ->
            val team = if (myTeam) r.team1 else r.team2
            val otherTeam = if (myTeam) r.team2 else r.team1

            if (enabled) {
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
            } else {
                team.declarations.remove(declaration)
            }
        }
    }

    fun setBidder(bidder: Int) = viewModelScope.launch {
        round.update { r ->
            r.team1.bidding = bidder == 0
            r.team2.bidding = bidder == 1
        }
    }

    fun updateScores(donelistener: () -> Unit) = viewModelScope.launch {
        round.value?.let { round ->
            gameRepository.addRound(round)
        }
        donelistener()
    }
}

data class AvailableDeclaration(
    val declaration: Declaration,
    var selected: Boolean = false
)