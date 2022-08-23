package ru.barsik.pictureslike

import android.app.Application
import androidx.room.Room
import ru.barsik.pictureslike.di.component.*
import ru.barsik.pictureslike.di.module.ApiModule
import ru.barsik.pictureslike.di.module.DatabaseModule
import ru.barsik.pictureslike.di.module.RepositoryModule
import ru.barsik.pictureslike.di.module.ViewModelModule
import ru.barsik.pictureslike.repo.AppDatabase

class App : Application() {

    private var viewModelComponent: ViewModelComponent? = null
    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_PicturesLike)
        initRoom()
        initDagger()
    }

    private fun initRoom(){
        database = Room.databaseBuilder(this, AppDatabase::class.java, "PicturesDB")
            .build()
    }

    private fun initDagger() {
        val apiComponent = DaggerApiComponent.builder()
            .apiModule(ApiModule())
            .build()
        val databaseComponent = DaggerDatabaseComponent.builder()
            .databaseModule(DatabaseModule(database!!))
            .build()
        val repositoryComponent = DaggerRepositoryComponent.builder()
            .apiComponent(apiComponent)
            .databaseComponent(databaseComponent)
            .repositoryModule(RepositoryModule())
            .build()
        viewModelComponent = DaggerViewModelComponent.builder()
            .repositoryComponent(repositoryComponent)
            .viewModelModule(ViewModelModule(this))
            .build()
    }
    fun getViewModelComponent(): ViewModelComponent {
        return this.viewModelComponent!!
    }
}