package com.platzi.android.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.domain.Character
import com.imagemaker.usecase.GetAllCharactersUseCase
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.CharacterGridAdapter
import com.imagemaker.requestmanager.APIConstants.BASE_API_URL
import com.imagemaker.databasemanager.CharacterDatabase
import com.imagemaker.databasemanager.CharacterLocalDataSourceImpl
import com.imagemaker.requestmanager.CharacterRemoteDataSourceImpl
import com.imagemaker.requestmanager.CharacterRequest
import com.platzi.android.rickandmorty.databinding.FragmentCharacterListBinding
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel
import com.platzi.android.rickandmorty.utils.setItemDecorationSpacing
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.fragment_character_list.*


class CharacterListFragment : Fragment() {

    private lateinit var characterGridAdapter: CharacterGridAdapter
    private lateinit var listener: OnCharacterListFragmentListener

    private  val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val characterRemoteDataSource : CharacterRemoteDataSource by lazy {
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

    private val getAllCharactersUseCase: GetAllCharactersUseCase by lazy {
        GetAllCharactersUseCase(characterRepository)
    }

    private val viewModel : CharacterListViewModel by lazy {
        CharacterListViewModel(getAllCharactersUseCase)
    }

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                viewModel.onLoadMoreItems(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnCharacterListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnCharacterListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return DataBindingUtil.inflate<FragmentCharacterListBinding>(
            inflater,
            R.layout.fragment_character_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@CharacterListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterGridAdapter = CharacterGridAdapter { character ->
            listener.openCharacterDetail(character)
        }
        characterGridAdapter.setHasStableIds(true)

        rvCharacterList.run{
            addOnScrollListener(onScrollListener)
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))

            adapter = characterGridAdapter
        }

        srwCharacterList.setOnRefreshListener {
            viewModel.onRetryGetAllCharacter(rvCharacterList.adapter?.itemCount ?: 0)
        }

        viewModel.model.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { navigation ->
                when(navigation){
                    CharacterListViewModel.CharacterListNavigation.ShowLoading -> {
                        srwCharacterList.isRefreshing = true
                    }
                    CharacterListViewModel.CharacterListNavigation.HideLoading -> {
                        srwCharacterList.isRefreshing = false
                    }
                    is CharacterListViewModel.CharacterListNavigation.ShowCharacterListError -> {
                        context?.showLongToast("Error")
                    }
                    is CharacterListViewModel.CharacterListNavigation.ShowCharacterList -> {
                        characterGridAdapter.addData(navigation.characterList)
                    }
                }
            }
        })

        viewModel.onGetAllCharacters()
    }


    interface OnCharacterListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = CharacterListFragment().apply {
            arguments = args
        }
    }

}
