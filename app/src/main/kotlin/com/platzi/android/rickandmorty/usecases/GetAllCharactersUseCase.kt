package com.platzi.android.rickandmorty.usecases

import com.imagemaker.domain.Character
import com.platzi.android.rickandmorty.api.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllCharactersUseCase (private val characterRequest: CharacterRequest){

    fun invoke(currentPage : Int) : Single<List<Character>> = characterRequest
        .getService<CharacterService>()
        .getAllCharacters(currentPage)
        .map(CharacterResponseServer::toCharacterDomainList)
        .observeOn(AndroidSchedulers.mainThread())
}