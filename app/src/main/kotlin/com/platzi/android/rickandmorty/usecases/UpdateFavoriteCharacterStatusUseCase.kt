package com.platzi.android.rickandmorty.usecases

import com.imagemaker.domain.Character
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.database.toCharacterEntity
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class UpdateFavoriteCharacterStatusUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke(character: Character) : Maybe<Boolean> {
        val characterEntity : CharacterEntity = character.toCharacterEntity()
        return  characterDao.getCharacterById(characterEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                if(isEmpty){
                    characterDao.insertCharacter(characterEntity)
                }else{
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}