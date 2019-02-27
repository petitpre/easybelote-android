package com.petitpre.easybelote.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.petitpre.easybelote.utils.Converters

@Database(entities = [Game::class, Round::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getAll(): LiveData<List<Game>>

    @Query("SELECT * FROM Game where id = :id")
    fun getFullGame(id: Long): LiveData<GameWithRound>

    @Query("SELECT * FROM Game where id = :id")
    fun get(id: Long): Game

    @Query("SELECT * FROM Game order by ID DESC limit 1")
    fun getLast(): Game?

    @Query("SELECT * FROM Round where id = :id")
    fun getRound(id: Long): Round?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game: Game): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(round: Round)

}