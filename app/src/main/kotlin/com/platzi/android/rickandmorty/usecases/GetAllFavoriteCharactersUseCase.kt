package com.platzi.android.rickandmorty.usecases

import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.domain.Character
import io.reactivex.Flowable

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllFavoriteCharactersUseCase(private val repository: CharacterRepository) {
    fun invoke() : Flowable<List<Character>> = repository.getAllFavoriteCharacters()
}