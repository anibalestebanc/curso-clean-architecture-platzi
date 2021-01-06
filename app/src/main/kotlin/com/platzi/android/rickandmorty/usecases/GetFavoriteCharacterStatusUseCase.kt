package com.platzi.android.rickandmorty.usecases

import com.imagemaker.data.repository.CharacterRepository
import io.reactivex.Maybe

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetFavoriteCharacterStatusUseCase(private val repository: CharacterRepository) {
    fun invoke(characterId: Int): Maybe<Boolean> =
        repository.getFavoriteCharacterStatus(characterId)
}