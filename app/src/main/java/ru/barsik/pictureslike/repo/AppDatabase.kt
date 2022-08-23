package ru.barsik.pictureslike.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.barsik.pictureslike.repo.model.PictureEntity
import ru.barsik.pictureslike.repo.model.PictureEntityDAO

@Database(entities = [PictureEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun pictureEntityDAO() : PictureEntityDAO
}