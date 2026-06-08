package com.phimapp.ui.search

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.phimapp.R
import com.phimapp.data.Result
import com.phimapp.databinding.FragmentSearchBinding
import com.phimapp.model.MovieItem
import com.phimapp.ui.MovieAdapter
import com.phimapp.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.GRID)
        binding.rvResults.apply {
            this.adapter = this@SearchFragment.adapter
            layoutManager = GridLayoutManager(context, 3)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        binding.btnSearch.setOnClickListener { performSearch() }

        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            adapter.submitList(emptyList())
            binding.tvEmpty.visibility = View.GONE
        }

        viewModel.results.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.tvEmpty.text = "Không tìm thấy kết quả"
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
                    adapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = result.message
                }
            }
        }
    }

    private fun performSearch() {
        val keyword = binding.etSearch.text?.toString()?.trim() ?: return
        if (keyword.isEmpty()) return
        hideKeyboard()
        viewModel.search(keyword)
    }

    private fun hideKeyboard() {
        requireContext().getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun navigateToDetail(movie: MovieItem) {
        findNavController().navigate(R.id.action_search_to_detail,
            Bundle().apply { putString("slug", movie.slug) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
