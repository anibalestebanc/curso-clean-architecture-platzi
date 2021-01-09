package com.platzi.android.rickandmorty.di

import com.imagemaker.usecase.GetEpisodeFromCharacterUseCase
import com.imagemaker.usecase.GetFavoriteCharacterStatusUseCase
import com.imagemaker.usecase.UpdateFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Anibal Cortez on 09-01-21.
 */

@Module
class CharacterDetailModule {

    @Provides
    fun characterDetailViewModelProvider(
        updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase,
        getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase,
        getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase
    ) = CharacterDetailViewModel(
        updateFavoriteCharacterStatusUseCase,
        getFavoriteCharacterStatusUseCase,
        getEpisodeFromCharacterUseCase
    )

}

@Subcomponent(modules = [(CharacterDetailModule::class)])
interface CharacterDetailComponent{

    val characterDetailViewModel : CharacterDetailViewModel
}