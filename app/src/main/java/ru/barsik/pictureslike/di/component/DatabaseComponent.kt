package ru.barsik.pictureslike.di.component

import dagger.Component
import ru.barsik.pictureslike.di.module.DatabaseModule
import ru.barsik.pictureslike.repo.AppDatabase

@Component(modules = [DatabaseModule::class])
interface DatabaseComponent {
    val database: AppDatabase
}