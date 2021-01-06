package com.platzi.android.rickandmorty.usecases

import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.domain.Character
import io.reactivex.Single

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllCharactersUseCase (private val repository: CharacterRepository){
    fun invoke(currentPage : Int) : Single<List<Character>> = repository.getAllCharacters(currentPage)
}