package ru.barsik.pictureslike.di.module

import dagger.Module
import dagger.Provides
import ru.barsik.pictureslike.di.scope.RepositoryScope
import ru.barsik.pictureslike.repo.AppDatabase
import ru.barsik.pictureslike.repo.AppRepository
import ru.barsik.pictureslike.repo.server.ServerCommunicator

@Module
class RepositoryModule {
    @RepositoryScope
    @Provides
    internal fun providesRepository(communicator: ServerCommunicator, database: AppDatabase) : AppRepository{
        return AppRepository(communicator, database)
    }
}