package com.petitpre.easybelote.ui

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.petitpre.easybelote.R
import com.petitpre.easybelote.model.Game
import com.petitpre.easybelote.model.GameRepository
import com.petitpre.easybelote.model.Team
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewGameViewModel(
    private val resources: Resources,
    private val gameRepository: GameRepository
) : CoroutineViewModel() {

    val maxScore = MutableLiveData<String>(resources.getString(R.string.new_game_nb_points_placeholder))
    val declarations = MutableLiveData<Boolean>(false)
    val players: List<MutableLiveData<String>> = (0..3).map { MutableLiveData<String>(null) }

    init {
        runBlocking {
            gameRepository.getLastGame()?.let { lastGame ->
                players[0].value = lastGame.team1.player1
                players[1].value = lastGame.team2.player1
                players[2].value = lastGame.team1.player2
                players[3].value = lastGame.team2.player2

                declarations.value = lastGame.declarations
                maxScore.value = lastGame.max_score.toString()
            }
        }
    }

    fun startGame(callback: (id: Long) -> Any) {
        viewModelScope.launch {
            val game = Game(
                max_score = maxScore.value?.toInt() ?: 1001,
                declarations = declarations.value ?: false
            )

            game.team1 = Team(
                players[0].value ?: resources.getString(R.string.new_game_players_you),
                players[2].value ?: resources.getString(R.string.new_game_players_your_partner)
            )
            game.team2 = Team(
                players[1].value ?: resources.getString(R.string.new_game_players_on_left),
                players[3].value ?: resources.getString(R.string.new_game_players_on_right)
            )
            val id = gameRepository.saveGame(game)

            callback(id)

        }
    }
}
