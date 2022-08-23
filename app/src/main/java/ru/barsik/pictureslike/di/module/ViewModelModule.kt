package ru.barsik.pictureslike.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.barsik.pictureslike.App
import ru.barsik.pictureslike.di.scope.ViewModelScope
import ru.barsik.pictureslike.domain.LikePicsViewModel
import ru.barsik.pictureslike.domain.RandomPicsViewModel
import ru.barsik.pictureslike.repo.AppRepository

@Module
class ViewModelModule(app: App) {

    internal var app: Application = app

    @ViewModelScope
    @Provides
    internal fun providesRandomPicsViewModel(repository: AppRepository): RandomPicsViewModel {
        return RandomPicsViewModel(app, repository)
    }

    @ViewModelScope
    @Provides
    internal fun providesLikePicsViewModule(repository: AppRepository): LikePicsViewModel {
        return LikePicsViewModel(app, repository)
    }
}