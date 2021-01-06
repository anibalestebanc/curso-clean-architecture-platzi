package com.imagemaker.usecase

import com.imagemaker.data.repository.EpisodeRepository
import com.imagemaker.domain.Episode
import io.reactivex.Single

/**
 * Created by Anibal Cortez on 05-01-21.
 */
class GetEpisodeFromCharacterUseCase(private val repository: EpisodeRepository) {
    fun invoke(episodeUrlList: List<String>): Single<List<Episode>> =
        repository.getEpisodeFromCharacter(episodeUrlList)
}