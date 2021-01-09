package com.platzi.android.rickandmorty.di

import com.imagemaker.usecase.GetAllFavoriteCharactersUseCase
import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Anibal Cortez on 09-01-21.
 */

@Module
class FavoriteListModule {

    @Provides
    fun favoriteListViewModelProvider(
        getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
    ) = FavoriteListViewModel(getAllFavoriteCharactersUseCase)

}

@Subcomponent(modules = [(FavoriteListModule::class)] )
interface FavoriteListComponent{
    val favoriteListViewModel : FavoriteListViewModel
}