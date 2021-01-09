package com.imagemaker.requestmanager

import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.domain.Character
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Anibal Cortez on 06-01-21.
 */
class CharacterRemoteDataSourceImpl(
    private val characterRequest: CharacterRequest
) : CharacterRemoteDataSource {


    override fun getAllCharacters(page: Int): Single<List<Character>> = characterRequest
        .getService<CharacterService>()
        .getAllCharacters(page)
        .map(CharacterResponseServer::toCharacterDomainList)
        .observeOn(AndroidSchedulers.mainThread())
}