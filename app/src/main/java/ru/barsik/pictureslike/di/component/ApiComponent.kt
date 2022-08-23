package ru.barsik.pictureslike.di.component

import dagger.Component
import ru.barsik.pictureslike.di.module.ApiModule
import ru.barsik.pictureslike.di.scope.ApiScope
import ru.barsik.pictureslike.repo.server.ServerCommunicator

@ApiScope
@Component(modules = [ApiModule::class])
interface ApiComponent {
    val serverCommunicator: ServerCommunicator
}