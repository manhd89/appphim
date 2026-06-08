package com.phimapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.phimapp.R
import com.phimapp.data.Result
import com.phimapp.databinding.FragmentHomeBinding
import com.phimapp.model.MovieItem
import com.phimapp.ui.MovieAdapter
import com.phimapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bannerAdapter: MovieAdapter
    private lateinit var newMoviesAdapter: MovieAdapter
    private lateinit var seriesAdapter: MovieAdapter
    private lateinit var singleAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeViewModels()
        setupClickListeners()
        viewModel.loadHomeData()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadHomeData()
        }
    }

    private fun setupAdapters() {
        bannerAdapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.BANNER)
        newMoviesAdapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.GRID)
        seriesAdapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.HORIZONTAL)
        singleAdapter = MovieAdapter({ navigateToDetail(it) }, MovieAdapter.LayoutType.HORIZONTAL)

        binding.rvBanner.apply {
            adapter = bannerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvNewMovies.apply {
            adapter = newMoviesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        binding.rvSeries.apply {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvSingle.apply {
            adapter = singleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModels() {
        viewModel.newMovies.observe(viewLifecycleOwner) { result ->
            binding.swipeRefresh.isRefreshing = false
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    bannerAdapter.submitList(result.data.take(8))
                    newMoviesAdapter.submitList(result.data.take(12))
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.seriesMovies.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success) {
                seriesAdapter.submitList(result.data.take(10))
            }
        }

        viewModel.singleMovies.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success) {
                singleAdapter.submitList(result.data.take(10))
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSeeAllNew.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_movieList,
                Bundle().apply { putString("type", "phim-moi-cap-nhat") })
        }
        binding.btnSeeAllSeries.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_movieList,
                Bundle().apply { putString("type", "phim-bo") })
        }
        binding.btnSeeAllSingle.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_movieList,
                Bundle().apply { putString("type", "phim-le") })
        }
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }
    }

    private fun navigateToDetail(movie: MovieItem) {
        findNavController().navigate(R.id.action_home_to_detail,
            Bundle().apply { putString("slug", movie.slug) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
