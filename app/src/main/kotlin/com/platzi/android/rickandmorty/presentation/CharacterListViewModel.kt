package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagemaker.domain.Character
import com.imagemaker.usecase.GetAllCharactersUseCase
import com.platzi.android.rickandmorty.presentation.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anibal Cortez on 04-01-21.
 */
class CharacterListViewModel(private val getAllCharactersUseCase: GetAllCharactersUseCase)
    : ViewModel() {

    private val disposable = CompositeDisposable()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false


    private val _model = MutableLiveData<Event<CharacterListNavigation>>()
    val model: LiveData<Event<CharacterListNavigation>>
        get() = _model


    sealed class CharacterListNavigation {
        object ShowLoading : CharacterListNavigation()
        object HideLoading : CharacterListNavigation()
        data class ShowCharacterListError(val error: Throwable) : CharacterListNavigation()
        data class ShowCharacterList(val characterList: List<Character>) :
            CharacterListNavigation()
    }

     fun onLoadMoreItems(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ) {
        if (isLoading || isLastPage || !isInFooter(
                visibleItemCount,
                firstVisibleItemPosition,
                totalItemCount
            )
        ) {
            return
        }

        currentPage += 1
        onGetAllCharacters()
    }

     fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
    }

    fun onRetryGetAllCharacter(itemCount: Int) {
        if (itemCount > 0) {
            _model.value =
                Event(
                    CharacterListNavigation.HideLoading
                )
            return
        }

        onGetAllCharacters()
    }

     fun onGetAllCharacters() {
        disposable.add(
            getAllCharactersUseCase
                .invoke(currentPage)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _model.value =
                        Event(
                            CharacterListNavigation.ShowLoading
                        )
                }
                .subscribe({ characterList ->
                    if (characterList.size < PAGE_SIZE) {
                        isLastPage = true
                    }

                    _model.value =
                        Event(
                            CharacterListNavigation.HideLoading
                        )
                    _model.value =
                        Event(
                            CharacterListNavigation.ShowCharacterList(characterList)
                        )
                }, { error ->
                    isLastPage = true
                    _model.value =
                        Event(
                            CharacterListNavigation.HideLoading
                        )
                    _model.value =
                        Event(
                            CharacterListNavigation.ShowCharacterListError(error)
                        )
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    companion object{
        private const val PAGE_SIZE = 20
    }

}