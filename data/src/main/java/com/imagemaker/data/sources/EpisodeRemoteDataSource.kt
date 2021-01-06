package com.imagemaker.data.sources

import com.imagemaker.domain.Episode
import io.reactivex.Single

/**
 * Created by Anibal Cortez on 06-01-21.
 */
interface EpisodeRemoteDataSource {
    fun getEpisodeFromCharacter(episodeUrlList : List<String>) : Single<List<Episode>>
}