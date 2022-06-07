package com.example.dictionary.search.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentSearchBinding
import com.example.dictionary.search.domain.model.SearchResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()


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

            included.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Toast.makeText(requireContext(), "fvghbjnmk", Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                }
                false
            })

            included.btnSearch.setOnClickListener {
                viewModel.checkKeyword(included.etSearch.text.toString())
            }
        }
    }


    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect {
                    when (it) {
                        is SearchState.Loading -> showLoading(true)
                        is SearchState.Success -> handleSuccess(it.searchResponse)
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

    private fun handleSuccess(searchResponse: SearchResponse) {
        showLoading(false)
        Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
    }

    private fun showError(error: String) {
        showLoading(false)
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

}