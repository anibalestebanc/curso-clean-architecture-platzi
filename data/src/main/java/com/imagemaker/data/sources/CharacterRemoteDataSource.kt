package com.imagemaker.data.sources

import com.imagemaker.domain.Character
import io.reactivex.Single

/**
 * Created by Anibal Cortez on 06-01-21.
 */
interface CharacterRemoteDataSource {
    fun getAllCharacters(page : Int) : Single<List<Character>>
}