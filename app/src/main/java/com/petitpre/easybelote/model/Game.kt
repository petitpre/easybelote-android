package com.petitpre.easybelote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val advert: Boolean = false
)

// 1 round = 162 points
// si bidder score > 81 -> bidder score += score
//                         non bidder score += 162-score

// si bidder score == 81 -> bidder score += 0
//                          non bidder score += 81

// si bidder score < 81 -> bidder score += 0
//                         non bidder score += 162