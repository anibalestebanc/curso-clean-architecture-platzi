package com.platzi.android.rickandmorty.database

import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 06-01-21.
 */
class CharacterLocalDataSourceImpl(
    private val database: CharacterDatabase
) : CharacterLocalDataSource {

    private val characterDao: CharacterDao by lazy { database.characterDao() }

    override fun getAllFavoriteCharacters(): Flowable<List<Character>> =
        characterDao
            .getAllFavoriteCharacters()
            .map(List<CharacterEntity>::toCharacterDomainList)
            .onErrorReturn { emptyList() }
            .subscribeOn(Schedulers.io())


    override fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean> =
        characterDao.getCharacterById(id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> {
        val characterEntity: CharacterEntity = character.toCharacterEntity()
        return characterDao.getCharacterById(characterEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                if (isEmpty) {
                    characterDao.insertCharacter(characterEntity)
                } else {
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}