package com.petitpre.easybelote.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val gameDao: GameDao) {

    suspend fun getGame(id: Long) = withContext(Dispatchers.IO) { gameDao.getById(id) }

    suspend fun saveGame(game: Game): Long = withContext(Dispatchers.IO) {
        gameDao.insert(game)
    }

    suspend fun addRound(  round: Round) = withContext(Dispatchers.IO) {
        gameDao.insert(round)
    }

    suspend fun getRound(id: Long) = withContext(Dispatchers.IO) { gameDao.getRoundById(id) }

}