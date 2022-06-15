package com.example.dictionary.search.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentSearchBinding
import com.example.dictionary.search.domain.model.SearchResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private var mediaPlayer: MediaPlayer? = null
    private var audioUrl: String? = null
    private var isPlaying = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        init()
        setListeners()
        observeState()
    }


    private fun init() {
        with(binding) {
            included.etSearch.showSoftInputOnFocus
            included.etSearch.requestFocus()
        }
    }


    private fun setListeners() {
        with(binding) {
            included.btnClear.setOnClickListener {
                included.etSearch.setText("")
            }

            included.btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            included.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isBlank()) {
                        included.btnClear.visibility = View.INVISIBLE
                    } else {
                        included.btnClear.visibility = View.VISIBLE
                    }
                }
            })

            /*included.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Toast.makeText(requireContext(), "fvghbjnmk", Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                }
                false
            })*/

            included.btnSearch.setOnClickListener {
                viewModel.checkKeyword(included.etSearch.text.toString())
            }

            btnPlayAudio.setOnClickListener {
                if (!audioUrl.isNullOrBlank()) {
//                    if (!isPlaying) {
                        isPlaying = true
                        mediaPlayer = MediaPlayer()
                        try {
                            mediaPlayer?.let { mediaPlayer ->
                                mediaPlayer.setDataSource(audioUrl)
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                            }
                        } catch (e: IOException) {
                            Log.e(LOG_TAG, "MediaPlayer prepare() failed")
                        }
//                    } else {
//                        isPlaying = false
//                        stopPlaying()
//                    }
                } else
                    Snackbar.make(binding.root, "No audio found", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect {
                    when (it) {
                        is SearchState.Loading -> showLoading(true)
                        is SearchState.SearchSuccess -> handleSearchSuccess(it.searchResponse)
                        is SearchState.Error -> showError(it.message)
                        is SearchState.Idle -> Unit
                    }
                }
            }
        }
    }

    private fun showLoading(flag: Boolean) {
        with(binding) {
            if (flag)
                included.progressBar.visibility = View.VISIBLE
            else
                included.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun handleSearchSuccess(searchResponse: SearchResponse) {
        showLoading(false)
        // Set data to UI
        binding.apply {
            // Set UI as visible
            cardResult.visibility = View.VISIBLE
            recyclerMeanings.visibility = View.VISIBLE

            tvWord.text = searchResponse.word

            // Check phonetics list
            searchResponse.phonetics.forEach { phonetic ->
                if (phonetic.audio.isNotBlank() && phonetic.text.isNotBlank()) {
                    tvPronunciation.text = phonetic.text
                    audioUrl = phonetic.audio
                }
            }

//            hbjn
//            hbnj
        }
    }

    private fun showError(error: String) {
        showLoading(false)
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

}