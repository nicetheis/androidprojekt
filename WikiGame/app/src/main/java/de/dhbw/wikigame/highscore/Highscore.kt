package de.dhbw.wikigame.highscore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highscores")
data class Highscore(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "score")
    var score: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}