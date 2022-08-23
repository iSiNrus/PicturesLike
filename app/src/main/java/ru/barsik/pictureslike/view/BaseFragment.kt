package ru.barsik.pictureslike.view

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.barsik.pictureslike.App
import ru.barsik.pictureslike.di.component.ViewModelComponent

abstract class BaseFragment(val application: Application) : Fragment(){
    protected abstract fun injectDependency(component: ViewModelComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDaggerDependencies()
    }

    private fun createDaggerDependencies(){
        injectDependency((application as App).getViewModelComponent())
    }
}