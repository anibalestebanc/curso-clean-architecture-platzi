package com.platzi.android.rickandmorty.di

import com.imagemaker.usecase.GetAllCharactersUseCase
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Anibal Cortez on 09-01-21.
 */
@Module
class CharacterListModule {

    @Provides
    fun characterListViewModelProvider(
        getAllCharactersUseCase: GetAllCharactersUseCase
    ) = CharacterListViewModel(getAllCharactersUseCase)


}

@Subcomponent(modules = [(CharacterListModule::class)] )
interface CharacterListComponent{
    val characterListViewModel : CharacterListViewModel
}
