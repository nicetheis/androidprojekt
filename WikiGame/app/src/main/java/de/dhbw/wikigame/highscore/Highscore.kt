package de.dhbw.wikigame.highscore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highscores")
data class Highscore(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "score")
    var score: Int,
    @ColumnInfo(name = "time")
    var time: Boolean,
    @ColumnInfo(name = "difficulty")
    var difficulty: Boolean,
    @ColumnInfo(name = "country")
    var country: String,

){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}