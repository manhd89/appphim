package com.phimapp.viewmodel

import androidx.lifecycle.*
import com.phimapp.data.MovieRepository
import com.phimapp.data.Result
import com.phimapp.model.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _newMovies = MutableLiveData<Result<List<MovieItem>>>()
    val newMovies: LiveData<Result<List<MovieItem>>> = _newMovies

    private val _seriesMovies = MutableLiveData<Result<List<MovieItem>>>()
    val seriesMovies: LiveData<Result<List<MovieItem>>> = _seriesMovies

    private val _singleMovies = MutableLiveData<Result<List<MovieItem>>>()
    val singleMovies: LiveData<Result<List<MovieItem>>> = _singleMovies

    private val _categories = MutableLiveData<Result<List<GenreItem>>>()
    val categories: LiveData<Result<List<GenreItem>>> = _categories

    fun loadHomeData() {
        loadNewMovies()
        loadSeriesMovies()
        loadSingleMovies()
        loadCategories()
    }

    private fun loadNewMovies() {
        _newMovies.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getNewMovies(1)
            _newMovies.value = when (result) {
                is Result.Success -> Result.Success(result.data.items)
                is Result.Error -> Result.Error(result.message)
                else -> Result.Error("Lỗi không xác định")
            }
        }
    }

    private fun loadSeriesMovies() {
        _seriesMovies.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getMovieList("phim-bo", 1)
            _seriesMovies.value = when (result) {
                is Result.Success -> Result.Success(result.data.data?.items ?: emptyList())
                is Result.Error -> Result.Error(result.message)
                else -> Result.Error("Lỗi không xác định")
            }
        }
    }

    private fun loadSingleMovies() {
        _singleMovies.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getMovieList("phim-le", 1)
            _singleMovies.value = when (result) {
                is Result.Success -> Result.Success(result.data.data?.items ?: emptyList())
                is Result.Error -> Result.Error(result.message)
                else -> Result.Error("Lỗi không xác định")
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val result = repository.getCategories()
            _categories.value = result
        }
    }
}

class MovieDetailViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _detail = MutableLiveData<Result<MovieDetailResponse>>()
    val detail: LiveData<Result<MovieDetailResponse>> = _detail

    fun loadDetail(slug: String) {
        _detail.value = Result.Loading
        viewModelScope.launch {
            _detail.value = repository.getMovieDetail(slug)
        }
    }
}

class SearchViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _results = MutableLiveData<Result<List<MovieItem>>>()
    val results: LiveData<Result<List<MovieItem>>> = _results

    fun search(keyword: String) {
        if (keyword.isBlank()) return
        _results.value = Result.Loading
        viewModelScope.launch {
            val result = repository.searchMovies(keyword)
            _results.value = when (result) {
                is Result.Success -> Result.Success(result.data.data?.items ?: emptyList())
                is Result.Error -> Result.Error(result.message)
                else -> Result.Error("Lỗi không xác định")
            }
        }
    }
}

class CategoryViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<Result<List<MovieItem>>>()
    val movies: LiveData<Result<List<MovieItem>>> = _movies

    private var currentPage = 1
    private var totalPages = 1
    private var currentSlug = ""
    private var isCategory = true

    fun loadByCategory(slug: String) {
        currentSlug = slug
        isCategory = true
        currentPage = 1
        _movies.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getMoviesByCategory(slug, 1)
            handleListResult(result)
        }
    }

    fun loadByCountry(slug: String) {
        currentSlug = slug
        isCategory = false
        currentPage = 1
        _movies.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getMoviesByCountry(slug, 1)
            handleListResult(result)
        }
    }

    fun loadNextPage() {
        if (currentPage >= totalPages) return
        currentPage++
        viewModelScope.launch {
            val result = if (isCategory)
                repository.getMoviesByCategory(currentSlug, currentPage)
            else
                repository.getMoviesByCountry(currentSlug, currentPage)
            handleListResult(result, append = true)
        }
    }

    private fun handleListResult(result: com.phimapp.data.Result<com.phimapp.network.ListWrapperResponse>, append: Boolean = false) {
        when (result) {
            is Result.Success -> {
                val items = result.data.data?.items ?: emptyList()
                totalPages = result.data.data?.params?.pagination?.totalPages ?: 1
                if (append) {
                    val current = (_movies.value as? Result.Success)?.data ?: emptyList()
                    _movies.value = Result.Success(current + items)
                } else {
                    _movies.value = Result.Success(items)
                }
            }
            is Result.Error -> _movies.value = Result.Error(result.message)
            else -> {}
        }
    }
}
