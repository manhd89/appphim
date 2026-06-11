package com.phimapp.viewmodel

import androidx.lifecycle.*
import com.phimapp.data.MovieRepository
import com.phimapp.data.Result
import com.phimapp.model.*
import kotlinx.coroutines.launch

// ─── HomeViewModel ────────────────────────────────────────────────────────────
class HomeViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _newMovies    = MutableLiveData<Result<List<MovieItem>>>()
    val newMovies: LiveData<Result<List<MovieItem>>> = _newMovies

    private val _seriesMovies = MutableLiveData<Result<List<MovieItem>>>()
    val seriesMovies: LiveData<Result<List<MovieItem>>> = _seriesMovies

    private val _singleMovies = MutableLiveData<Result<List<MovieItem>>>()
    val singleMovies: LiveData<Result<List<MovieItem>>> = _singleMovies

    fun loadHomeData() {
        loadNewMovies()
        loadSection("phim-bo",  _seriesMovies)
        loadSection("phim-le",  _singleMovies)
    }

    private fun loadNewMovies() {
        _newMovies.value = Result.Loading
        viewModelScope.launch {
            _newMovies.value = repo.getNewMovies(1)
        }
    }

    private fun loadSection(type: String, target: MutableLiveData<Result<List<MovieItem>>>) {
        target.value = Result.Loading
        viewModelScope.launch {
            target.value = when (val r = repo.getMovieList(type, 1)) {
                is Result.Success -> Result.Success(r.data.data?.items ?: emptyList())
                is Result.Error   -> Result.Error(r.message)
                else              -> Result.Error("Lỗi không xác định")
            }
        }
    }
}

// ─── MovieDetailViewModel ─────────────────────────────────────────────────────
class MovieDetailViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _detail = MutableLiveData<Result<MovieDetailResponse>>()
    val detail: LiveData<Result<MovieDetailResponse>> = _detail

    fun loadDetail(slug: String) {
        _detail.value = Result.Loading
        viewModelScope.launch {
            _detail.value = repo.getMovieDetail(slug)
        }
    }
}

// ─── SearchViewModel ──────────────────────────────────────────────────────────
class SearchViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _results = MutableLiveData<Result<List<MovieItem>>>()
    val results: LiveData<Result<List<MovieItem>>> = _results

    fun search(keyword: String) {
        if (keyword.isBlank()) return
        _results.value = Result.Loading
        viewModelScope.launch {
            _results.value = when (val r = repo.searchMovies(keyword)) {
                is Result.Success -> Result.Success(r.data.data?.items ?: emptyList())
                is Result.Error   -> Result.Error(r.message)
                else              -> Result.Error("Lỗi không xác định")
            }
        }
    }
}

// ─── CategoryViewModel — dùng cho thể loại, quốc gia, danh sách tổng hợp ────
class CategoryViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _movies = MutableLiveData<Result<List<MovieItem>>>()
    val movies: LiveData<Result<List<MovieItem>>> = _movies

    private var currentPage = 1
    private var totalPages  = 1
    private var currentSlug = ""
    private var mode        = Mode.CATEGORY

    enum class Mode { CATEGORY, COUNTRY, TYPE }

    fun loadByCategory(slug: String) { load(slug, Mode.CATEGORY) }
    fun loadByCountry(slug: String)  { load(slug, Mode.COUNTRY) }
    fun loadByType(type: String)     { load(type, Mode.TYPE) }

    private fun load(slug: String, m: Mode) {
        currentSlug = slug
        mode        = m
        currentPage = 1
        _movies.value = Result.Loading
        viewModelScope.launch { fetch(append = false) }
    }

    fun loadNextPage() {
        if (currentPage >= totalPages) return
        currentPage++
        viewModelScope.launch { fetch(append = true) }
    }

    private suspend fun fetch(append: Boolean) {
        val result = when (mode) {
            Mode.CATEGORY -> repo.getMoviesByCategory(currentSlug, currentPage)
            Mode.COUNTRY  -> repo.getMoviesByCountry(currentSlug, currentPage)
            Mode.TYPE     -> repo.getMovieList(currentSlug, currentPage)
        }
        when (result) {
            is Result.Success -> {
                val items = result.data.data?.items ?: emptyList()
                totalPages = result.data.data?.params?.pagination?.totalPages ?: 1
                _movies.value = if (append) {
                    val old = (_movies.value as? Result.Success)?.data ?: emptyList()
                    Result.Success(old + items)
                } else {
                    Result.Success(items)
                }
            }
            is Result.Error -> _movies.value = Result.Error(result.message)
            else -> {}
        }
    }
}
