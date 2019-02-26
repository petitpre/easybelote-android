package com.petitpre.easybelote.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.petitpre.easybelote.R
import java.util.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Date = Date(),

    @Embedded(prefix = "team_one_")
    var team1: Team = Team(),
    @Embedded(prefix = "team_two_")
    var team2: Team = Team(),

    val max_score: Int = 1001,
    val declarations: Boolean = false,

    var bidder: Int = 0
)

data class Team(
    val player1: String? = null,
    val player2: String? = null
)

@Entity
data class Round(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val gameId: Long,

    var bidder: Int = 0,

    @Embedded(prefix = "team_one_")
    var team1: TeamScore = TeamScore(),

    @Embedded(prefix = "team_two_")
    var team2: TeamScore = TeamScore()

)

data class TeamScore(
    var score: Long = 0,
    var declarations: MutableSet<Declaration> = mutableSetOf()
) {
    val totalScore: Long
        get() {
            var total = score
            if (declarations.contains(Declaration.Belote)) total += 20
            if (declarations.contains(Declaration.Tierce)) total += 20
            if (declarations.contains(Declaration.Quarte)) total += 50
            if (declarations.contains(Declaration.Quinte)) total += 100
            if (declarations.contains(Declaration.Square)) total += 100
            if (declarations.contains(Declaration.CarreOfNine)) total += 150
            if (declarations.contains(Declaration.CarreOfJacks)) total += 200
            return total
        }
}

data class GameWithRound(
    @Embedded
    val game: Game,
    @Relation(parentColumn = "id", entityColumn = "gameId", entity = Round::class)
    val rounds: List<Round>
) {
    fun getMyScore(): Long {
        return rounds.map { it.team1.totalScore }.sum()
    }

    fun getOtherScore(): Long {
        return rounds.map { it.team2.totalScore }.sum()
    }
}

enum class Declaration(val text: Int) {
    Belote(R.string.game_round_declaration_belote),
    Capot(R.string.game_round_declaration_capot),
    Tierce(R.string.game_round_declaration_tierce),
    Quarte(R.string.game_round_declaration_quarte),
    Quinte(R.string.game_round_declaration_quinte),
    Square(R.string.game_round_declaration_square),
    CarreOfNine(R.string.game_round_declaration_squareNines),
    CarreOfJacks(R.string.game_round_declaration_squareJacks)
}