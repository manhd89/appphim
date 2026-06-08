package com.phimapp.ui.category

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phimapp.R
import com.phimapp.data.Result
import com.phimapp.databinding.FragmentCategoryBinding
import com.phimapp.model.MovieItem
import com.phimapp.ui.MovieAdapter
import com.phimapp.viewmodel.CategoryViewModel

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slug = arguments?.getString("slug") ?: return
        val title = arguments?.getString("title") ?: "Danh sách phim"
        val isCategory = arguments?.getBoolean("isCategory", true) ?: true

        binding.tvTitle.text = title
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        movieAdapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.GRID)
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(context, 3)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    if (!rv.canScrollVertically(1) && !isLoading) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }

        viewModel.movies.observe(viewLifecycleOwner) { result ->
            isLoading = result is Result.Loading
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    movieAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        if (isCategory) viewModel.loadByCategory(slug)
        else viewModel.loadByCountry(slug)
    }

    private fun navigateToDetail(movie: MovieItem) {
        findNavController().navigate(R.id.action_category_to_detail,
            Bundle().apply { putString("slug", movie.slug) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
