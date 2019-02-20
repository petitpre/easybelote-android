package com.petitpre.easybelote.model

import androidx.room.*
import com.petitpre.easybelote.R
import java.util.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Date = Date(),

    var player_one: String? = null,
    var player_two: String? = null,
    var player_three: String? = null,
    var player_four: String? = null,

    val max_score: Int = 1001,
    val declarations: Boolean = false,

    var bidder: Int = 0
)

@Entity
data class Round(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val gameId: Long,
    var myScore: Long,
    var otherScore: Long,

    var myDeclarations: List<Declaration> = emptyList(),
    var otherDeclarations: List<Declaration> = emptyList()
)

data class GameWithRound(
    @Embedded
    val game: Game,
    @Relation(parentColumn = "id", entityColumn = "gameId", entity = Round::class)
    val rounds: List<Round>
) {
    fun getMyScore(): Long {
        return rounds.map { it.myScore }.sum()
    }

    fun getOtherScore(): Long {
        return rounds.map { it.otherScore }.sum()
    }
}

enum class Declaration {
    tierce,
    quarte,
    quinte,
    carre,
    carre_of_nine,
    carre_of_aces
}