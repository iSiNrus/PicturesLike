package ru.barsik.pictureslike.di.component

import dagger.Component
import ru.barsik.pictureslike.di.module.ViewModelModule
import ru.barsik.pictureslike.di.scope.ViewModelScope
import ru.barsik.pictureslike.view.LikePicsFrag
import ru.barsik.pictureslike.view.RandomPicsFrag

@ViewModelScope
@Component(
    modules = [ViewModelModule::class],
    dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {
    fun inject(fragment: RandomPicsFrag)
    fun inject(fragment: LikePicsFrag)
}