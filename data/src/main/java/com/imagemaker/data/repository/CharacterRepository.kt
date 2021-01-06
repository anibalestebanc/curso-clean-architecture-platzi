package com.imagemaker.data.repository

import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.domain.Character
import io.reactivex.Single

/**
 * Created by Anibal Cortez on 06-01-21.
 */
class CharacterRepository(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) {

    fun getAllCharacters(page: Int): Single<List<Character>> =
        remoteDataSource.getAllCharacters(page)

    fun getAllFavoriteCharacters() = localDataSource.getAllFavoriteCharacters()

    fun getFavoriteCharacterStatus(id: Int) = localDataSource.getFavoriteCharacterStatus(id)

    fun updateFavoriteCharacterStatus(character: Character) =
        localDataSource.updateFavoriteCharacterStatus(character)

}