package com.imagemaker.usecase

import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.data.repository.EpisodeRepository
import dagger.Module
import dagger.Provides

/**
 * Created by Anibal Cortez on 09-01-21.
 */

@Module
class UseCaseModule {

    @Provides
    fun getAllCharacterUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllCharactersUseCase(characterRepository)

    @Provides
    fun getAllFavoriteCharactersUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllFavoriteCharactersUseCase(characterRepository)

    @Provides
    fun getEpisodeFromCharacterUseCaseProvider(episodeRepository: EpisodeRepository) =
        GetEpisodeFromCharacterUseCase(episodeRepository)


    @Provides
    fun getFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) =
        GetFavoriteCharacterStatusUseCase(characterRepository)

    @Provides
    fun updateFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) =
        UpdateFavoriteCharacterStatusUseCase(characterRepository)
}