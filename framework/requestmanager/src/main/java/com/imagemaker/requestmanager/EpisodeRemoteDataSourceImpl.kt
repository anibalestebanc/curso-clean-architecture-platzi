package com.imagemaker.requestmanager

import com.imagemaker.data.sources.EpisodeRemoteDataSource
import com.imagemaker.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 06-01-21.
 */
class EpisodeRemoteDataSourceImpl(
    private val episodeRequest: EpisodeRequest
) : EpisodeRemoteDataSource {

    override fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> {
        return Observable.fromIterable(episodeUrlList)
            .flatMap { episode: String ->
                episodeRequest.baseUrl = episode
                episodeRequest
                    .getService<EpisodeService>()
                    .getEpisode()
                    .map(EpisodeServer::toEpisodeDomain)
                    .toObservable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

}