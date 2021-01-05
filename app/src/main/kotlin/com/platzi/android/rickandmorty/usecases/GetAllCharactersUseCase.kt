package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.api.CharacterRequest
import com.platzi.android.rickandmorty.api.CharacterResponseServer
import com.platzi.android.rickandmorty.api.CharacterService
import com.platzi.android.rickandmorty.api.toCharacterServerList
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetAllCharactersUseCase (private val characterRequest: CharacterRequest){

    fun invoke(currentPage : Int) = characterRequest
        .getService<CharacterService>()
        .getAllCharacters(currentPage)
        .map(CharacterResponseServer::toCharacterServerList)
        .observeOn(AndroidSchedulers.mainThread())
}