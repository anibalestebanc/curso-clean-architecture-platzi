package com.platzi.android.rickandmorty.usecases

import com.imagemaker.domain.Character
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.database.toCharacterDomainList
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllFavoriteCharactersUseCase(private val characterDao: CharacterDao) {

    fun invoke() : Flowable<List<Character>> =  characterDao
        .getAllFavoriteCharacters()
        .map(List<CharacterEntity>::toCharacterDomainList)
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())
}