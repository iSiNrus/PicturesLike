package ru.barsik.pictureslike.di.component

import dagger.Component
import ru.barsik.pictureslike.di.module.RepositoryModule
import ru.barsik.pictureslike.di.scope.RepositoryScope
import ru.barsik.pictureslike.repo.AppRepository

@RepositoryScope
@Component(
    modules = [RepositoryModule::class],
    dependencies = [ApiComponent::class, DatabaseComponent::class]
)
interface RepositoryComponent {
    val repository: AppRepository
}