package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.*
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.presentation.util.Event
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Anibal Cortez on 04-01-21.
 */
class CharacterDetailViewModel(private val characterDao: CharacterDao, private val episodeRequest: EpisodeRequest)
    : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _model = MutableLiveData<Event<CharacterDetailNavigation>>()
    val model: LiveData<Event<CharacterDetailNavigation>> get() = _model

    sealed class CharacterDetailNavigation{
        object ShowProgressBar : CharacterDetailNavigation()
        object HideProgressBar : CharacterDetailNavigation()
        data class UpdateFavoriteIcon(val isFavorite: Boolean) : CharacterDetailNavigation()
        data class ShowEpisodeList(val episodeList : List<EpisodeServer>) : CharacterDetailNavigation()
        data class ShowEpisodeError(val error: Throwable) : CharacterDetailNavigation()
    }

     fun onValidateFavoriteCharacterStatus(character: CharacterServer){
        disposable.add(
            characterDao.getCharacterById(character.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    Maybe.just(!isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                   // updateFavoriteIcon(isFavorite)
                    _model.value = Event(CharacterDetailNavigation.UpdateFavoriteIcon(isFavorite))
                }
        )
    }

    fun onShowEpisodeList(episodeUrlList: List<String>){
        disposable.add(
            Observable.fromIterable(episodeUrlList)
                .flatMap { episode: String ->
                    episodeRequest.baseUrl = episode
                    episodeRequest
                        .getService<EpisodeService>()
                        .getEpisode()
                        .toObservable()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                   // episodeProgressBar.isVisible = true
                    _model.value = Event(CharacterDetailNavigation.ShowProgressBar)

                }
                .subscribe(
                    { episodeList ->
                       //episodeProgressBar.isVisible = false
                        //episodeListAdapter.updateData(episodeList)

                        _model.value = Event(CharacterDetailNavigation.HideProgressBar)
                        _model.value = Event(CharacterDetailNavigation.ShowEpisodeList(episodeList))

                    },
                    { error ->
                       // episodeProgressBar.isVisible = false
                        //this@CharacterDetailActivity.showLongToast("Error -> ${error.message}")

                        _model.value = Event(CharacterDetailNavigation.HideProgressBar)
                        _model.value = Event(CharacterDetailNavigation.ShowEpisodeError(error))

                    })
        )
    }

    fun onUpdateFavoriteCharacterStatus(character: CharacterServer) {
        val characterEntity: CharacterEntity = character.toCharacterEntity()
        disposable.add(
            characterDao.getCharacterById(characterEntity.id)
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
                .subscribe { isFavorite ->
                   // updateFavoriteIcon(isFavorite)
                    _model.value = Event(CharacterDetailNavigation.UpdateFavoriteIcon(isFavorite))
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}