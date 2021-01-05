package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.*
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.presentation.util.Event
import com.platzi.android.rickandmorty.usecases.GetFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Anibal Cortez on 04-01-21.
 */
class CharacterDetailViewModel(
    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase,
    private val getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _model = MutableLiveData<Event<CharacterDetailNavigation>>()
    val model: LiveData<Event<CharacterDetailNavigation>> get() = _model

    sealed class CharacterDetailNavigation {
        object ShowProgressBar : CharacterDetailNavigation()
        object HideProgressBar : CharacterDetailNavigation()
        data class UpdateFavoriteIcon(val isFavorite: Boolean) : CharacterDetailNavigation()
        data class ShowEpisodeList(val episodeList: List<EpisodeServer>) :
            CharacterDetailNavigation()

        data class ShowEpisodeError(val error: Throwable) : CharacterDetailNavigation()
    }

    fun onValidateFavoriteCharacterStatus(character: CharacterServer) {
        disposable.add(
            getFavoriteCharacterStatusUseCase
                .invoke(character.id)
                .subscribe { isFavorite ->
                    // updateFavoriteIcon(isFavorite)
                    _model.value = Event(CharacterDetailNavigation.UpdateFavoriteIcon(isFavorite))
                }
        )
    }

    fun onShowEpisodeList(episodeUrlList: List<String>) {
        disposable.add(
            getEpisodeFromCharacterUseCase
                .invoke(episodeUrlList)
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
            updateFavoriteCharacterStatusUseCase
                .invoke(characterEntity)
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