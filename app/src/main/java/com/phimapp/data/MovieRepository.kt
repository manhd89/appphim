package com.phimapp.data

import com.phimapp.model.*
import com.phimapp.network.ListWrapperResponse
import com.phimapp.network.RetrofitClient

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class MovieRepository {

    private val api = RetrofitClient.service

    suspend fun getNewMovies(page: Int = 1): Result<MovieListResponse> {
        return try {
            val response = api.getNewMovies(page)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được danh sách phim")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getMovieDetail(slug: String): Result<MovieDetailResponse> {
        return try {
            val response = api.getMovieDetail(slug)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được thông tin phim")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getMovieList(type: String, page: Int = 1): Result<ListWrapperResponse> {
        return try {
            val response = api.getMovieList(type, page)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được danh sách phim")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun searchMovies(keyword: String, page: Int = 1): Result<SearchResponse> {
        return try {
            val response = api.searchMovies(keyword, page)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tìm được phim")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getCategories(): Result<List<GenreItem>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được thể loại")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getCountries(): Result<List<GenreItem>> {
        return try {
            val response = api.getCountries()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được quốc gia")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getMoviesByCategory(slug: String, page: Int = 1): Result<ListWrapperResponse> {
        return try {
            val response = api.getMoviesByCategory(slug, page)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được phim theo thể loại")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }

    suspend fun getMoviesByCountry(slug: String, page: Int = 1): Result<ListWrapperResponse> {
        return try {
            val response = api.getMoviesByCountry(slug, page)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Không tải được phim theo quốc gia")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Lỗi kết nối")
        }
    }
}
