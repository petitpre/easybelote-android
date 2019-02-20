package com.petitpre.easybelote.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.petitpre.easybelote.utils.Converters

@Database(entities = arrayOf(Game::class, Round::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): LiveData<List<Game>>

    @Query("SELECT * FROM game where id = :id")
    fun getById(id: Long): LiveData<GameWithRound>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game: Game): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(round: Round)
}