package com.imagemaker.usecase

import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.domain.Character
import io.reactivex.Maybe

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class UpdateFavoriteCharacterStatusUseCase(private val repository: CharacterRepository) {
    fun invoke(character: Character): Maybe<Boolean> =
        repository.updateFavoriteCharacterStatus(character)
}