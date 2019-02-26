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
            if (declarations.contains(Declaration.belote)) total += 20

            if (declarations.contains(Declaration.tierce)) total += 20
            if (declarations.contains(Declaration.quarte)) total += 50
            if (declarations.contains(Declaration.quinte)) total += 100
            if (declarations.contains(Declaration.square)) total += 100
            if (declarations.contains(Declaration.carre_of_nine)) total += 150
            if (declarations.contains(Declaration.carre_of_jacks)) total += 200

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
    belote(R.string.game_round_declaration_belote),
    capot(R.string.game_round_declaration_capot),
    tierce(R.string.game_round_declaration_tierce),
    quarte(R.string.game_round_declaration_quarte),
    quinte(R.string.game_round_declaration_quinte),
    square(R.string.game_round_declaration_square),
    carre_of_nine(R.string.game_round_declaration_squareNines),
    carre_of_jacks(R.string.game_round_declaration_squareJacks)
}