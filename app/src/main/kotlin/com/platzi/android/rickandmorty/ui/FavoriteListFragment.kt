package com.platzi.android.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.domain.Character
import com.imagemaker.usecase.GetAllFavoriteCharactersUseCase
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.FavoriteListAdapter
import com.imagemaker.requestmanager.APIConstants.BASE_API_URL
import com.imagemaker.requestmanager.CharacterRemoteDataSourceImpl
import com.imagemaker.requestmanager.CharacterRequest
import com.imagemaker.databasemanager.CharacterDatabase
import com.imagemaker.databasemanager.CharacterLocalDataSourceImpl
import com.platzi.android.rickandmorty.databinding.FragmentFavoriteListBinding
import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel
import com.platzi.android.rickandmorty.utils.setItemDecorationSpacing
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener


    private  val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val characterRemoteDataSource: CharacterRemoteDataSource by lazy {
        CharacterRemoteDataSourceImpl(
            characterRequest
        )
    }

    private val characterLocalDataSource: CharacterLocalDataSource by lazy {
        CharacterLocalDataSourceImpl(
            CharacterDatabase.getDatabase(activity!!.applicationContext)
        )
    }

    private val characterRepository : CharacterRepository by lazy {
        CharacterRepository(characterRemoteDataSource, characterLocalDataSource)
    }

    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase by lazy {
        GetAllFavoriteCharactersUseCase(characterRepository)
    }

    private val viewModel : FavoriteListViewModel by lazy {
        FavoriteListViewModel(getAllFavoriteCharactersUseCase)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnFavoriteListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnFavoriteListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@FavoriteListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = FavoriteListAdapter { character ->
            listener.openCharacterDetail(character)
        }
        favoriteListAdapter.setHasStableIds(true)

        rvFavoriteList.run {
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))
            adapter = favoriteListAdapter
        }

        viewModel.characterFavoriteList.observe(this, Observer(viewModel::onFavoriteCharacterList) )

        viewModel.model.observe(this, Observer { events->
            events?.getContentIfNotHandled()?.let { navigation->
                when(navigation){
                    is FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList -> {
                        tvEmptyListMessage.isVisible = false
                        favoriteListAdapter.updateData(navigation.characterList)
                    }
                    FavoriteListViewModel.FavoriteListNavigation.ShowEmptyList -> {
                        tvEmptyListMessage.isVisible = true
                        favoriteListAdapter.updateData(emptyList())
                    }

                }
            }

        })
    }


    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: Character)
    }


    companion object {

        fun newInstance(args: Bundle? = Bundle()) = FavoriteListFragment().apply {
            arguments = args
        }
    }


}
