package de.dhbw.wikigame.highscore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HighscoreDao {
    @Query("Select * from highscores")
    fun getAll(): List<Highscore>

    @Query("Select * from highscores order by score desc")
    fun getAllSortedDESC(): List<Highscore>

    @Query("Select * from highscores where time = 1 order by score desc")
    fun getAllForTimeSortedDESC(): List<Highscore>

    @Query("Select * from highscores where difficulty = 0 order by score desc")
    fun getAllForEasySortedDESC(): List<Highscore>

    @Query("Select * from highscores where difficulty = 1 order by score desc")
    fun getAllForHeavySortedDESC(): List<Highscore>

    @Query("Select * from highscores limit 5")
    fun getTopFive(): List<Highscore>

    @Insert
    fun insertAll(vararg scores: Highscore)

    @Insert
    fun insertOne(score: Highscore)

    @Query("Delete from highscores")
    fun deleteAll()
}