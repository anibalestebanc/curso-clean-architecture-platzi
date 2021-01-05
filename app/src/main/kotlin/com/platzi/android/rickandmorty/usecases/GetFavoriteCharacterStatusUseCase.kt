package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetFavoriteCharacterStatusUseCase(private val characterDao: CharacterDao) {

    fun invoke(characterId : Int) :Maybe<Boolean> =   characterDao.getCharacterById(characterId)
        .isEmpty
        .flatMapMaybe { isEmpty ->
            Maybe.just(!isEmpty)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}