package de.dhbw.wikigame.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.dhbw.wikigame.highscore.Highscore
import de.dhbw.wikigame.highscore.HighscoreDao

@Database(entities = [Highscore::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getHighscoreDao(): HighscoreDao
}