package com.petitpre.easybelote.ui

import android.content.res.Resources
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.petitpre.easybelote.R
import com.petitpre.easybelote.model.Game
import com.petitpre.easybelote.model.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewGameViewModel(
    private val resources: Resources,
    private val gameRepository: GameRepository
) : CoroutineViewModel() {

    val maxScore = MutableLiveData<String>(1001.toString())
    val declarations = MutableLiveData<Boolean>(false)
    val players: List<MutableLiveData<String>> = (0..3).map { MutableLiveData<String>(null) }

    fun startGame(callback: (id: Long) -> Any) {
        viewModelScope.launch {
            val game = Game(
                max_score = maxScore.value?.toInt() ?: 1001,
                declarations = declarations.value ?: false
            )

            game.player_one = players[0].value ?: resources.getString(R.string.you)
            game.player_two = players[1].value ?: resources.getString(R.string.left)
            game.player_three = players[2].value ?: resources.getString(R.string.partner)
            game.player_four = players[3].value ?: resources.getString(R.string.right)

            val id = gameRepository.saveGame(game)

            callback(id)

        }
    }
}
