package ru.barsik.pictureslike.di.module

import dagger.Module
import dagger.Provides
import ru.barsik.pictureslike.repo.AppDatabase

@Module()
class DatabaseModule(private val appDatabase: AppDatabase) {
    @Provides
    internal fun provideRoomDatabase() : AppDatabase{
        return appDatabase
    }
}