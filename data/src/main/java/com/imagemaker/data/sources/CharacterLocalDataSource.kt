package com.imagemaker.data.sources

import com.imagemaker.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by Anibal Cortez on 06-01-21.
 */
interface CharacterLocalDataSource {
    fun getAllFavoriteCharacters(): Flowable<List<Character>>
    fun getFavoriteCharacterStatus(id : Int): Maybe<Boolean>
    fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean>
}