package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllFavoriteCharactersUseCase(private val characterDao: CharacterDao) {

    fun invoke() =  characterDao
        .getAllFavoriteCharacters()
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())
}