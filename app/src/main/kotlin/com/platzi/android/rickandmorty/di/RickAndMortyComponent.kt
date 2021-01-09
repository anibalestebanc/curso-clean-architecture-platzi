package com.platzi.android.rickandmorty.di

import android.app.Application
import com.imagemaker.data.RepositoryModule
import com.imagemaker.databasemanager.DatabaseModule
import com.imagemaker.requestmanager.ApiModule
import com.imagemaker.usecase.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Anibal Cortez on 09-01-21.
 */
@Singleton
@Component(modules = [ApiModule::class, DatabaseModule::class, RepositoryModule::class, UseCaseModule::class])
interface RickAndMortyComponent {

    fun inject(module: CharacterListModule): CharacterListComponent
    fun inject(module: FavoriteListModule): FavoriteListComponent
    fun inject(module: CharacterDetailModule) : CharacterDetailComponent

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance app : Application) : RickAndMortyComponent
    }

}