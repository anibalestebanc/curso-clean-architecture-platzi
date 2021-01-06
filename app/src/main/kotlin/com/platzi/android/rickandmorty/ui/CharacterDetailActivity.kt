package com.platzi.android.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.data.repository.EpisodeRepository
import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.data.sources.EpisodeRemoteDataSource
import com.imagemaker.domain.Character
import com.imagemaker.usecase.GetEpisodeFromCharacterUseCase
import com.imagemaker.usecase.GetFavoriteCharacterStatusUseCase
import com.imagemaker.usecase.UpdateFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.EpisodeListAdapter
import com.platzi.android.rickandmorty.api.APIConstants.BASE_API_URL
import com.platzi.android.rickandmorty.api.CharacterRemoteDataSourceImpl
import com.platzi.android.rickandmorty.api.CharacterRequest
import com.platzi.android.rickandmorty.api.EpisodeRemoteDataSourceImpl
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.database.CharacterLocalDataSourceImpl
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.presentation.mapper.toDomainCharacter
import com.platzi.android.rickandmorty.presentation.model.PresentationCharacter
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.bindCircularImageUrl
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {


    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding

    private var character : Character? = null

    private  val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }

    private  val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val characterRemoteDataSource : CharacterRemoteDataSource by lazy {
        CharacterRemoteDataSourceImpl(characterRequest)
    }

    private val characterLocalDataSource: CharacterLocalDataSource by lazy {
        CharacterLocalDataSourceImpl(CharacterDatabase.getDatabase(applicationContext))
    }

    private val characterRepository : CharacterRepository by lazy {
        CharacterRepository(characterRemoteDataSource, characterLocalDataSource)
    }

    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val episodeRemoteDataSource: EpisodeRemoteDataSource by lazy {
        EpisodeRemoteDataSourceImpl(episodeRequest)
    }

    private val episodeRepository: EpisodeRepository by lazy {
        EpisodeRepository(episodeRemoteDataSource)
    }

    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRepository)
    }

    private val getCharacterByIdUseCase: GetFavoriteCharacterStatusUseCase by lazy {
        GetFavoriteCharacterStatusUseCase(characterRepository)
    }


    private val viewModel: CharacterDetailViewModel by lazy {
        CharacterDetailViewModel(
            updateFavoriteCharacterStatusUseCase,
            getCharacterByIdUseCase,
            getEpisodeFromCharacterUseCase
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        character = intent?.getParcelableExtra<PresentationCharacter>(Constants.EXTRA_CHARACTER)?.toDomainCharacter()

        if (character == null) {
            this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
            finish()
            return
        }



        viewModel.model.observe(this, Observer { events ->
            events?.getContentIfNotHandled()?.let { navigation ->
                when (navigation) {
                    CharacterDetailViewModel.CharacterDetailNavigation.ShowProgressBar -> {
                        episodeProgressBar.isVisible = true
                    }
                    CharacterDetailViewModel.CharacterDetailNavigation.HideProgressBar -> {
                        episodeProgressBar.isVisible = false
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.UpdateFavoriteIcon -> {
                        updateFavoriteIcon(navigation.isFavorite)
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeList -> {
                        episodeListAdapter.updateData(navigation.episodeList)
                    }
                    is CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeError -> {
                        this@CharacterDetailActivity.showLongToast("Error -> ${navigation.error.message}")
                    }
                }

            }

        })


        viewModel.onValidateFavoriteCharacterStatus(character!!)

        binding.characterImage.bindCircularImageUrl(
            url = character!!.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character!!.name
        binding.characterDataStatus = character!!.status
        binding.characterDataSpecies = character!!.species
        binding.characterDataGender = character!!.gender
        binding.characterDataOriginName = character!!.origin.name
        binding.characterDataLocationName = character!!.location.name

        viewModel.onShowEpisodeList(character!!.episodeList)

        characterFavorite.setOnClickListener {
            viewModel.onUpdateFavoriteCharacterStatus(character!!)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun updateFavoriteIcon(isFavorite: Boolean?) {
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

}
