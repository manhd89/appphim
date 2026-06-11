package com.phimapp.network

import com.phimapp.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface PhimApiService {

    // Phim mới cập nhật V3 — poster_url đã full URL
    @GET("danh-sach/phim-moi-cap-nhat-v3")
    suspend fun getNewMovies(@Query("page") page: Int = 1): Response<MovieListResponse>

    // Chi tiết phim + danh sách tập
    @GET("phim/{slug}")
    suspend fun getMovieDetail(@Path("slug") slug: String): Response<MovieDetailResponse>

    // Danh sách theo loại: phim-bo, phim-le, tv-shows, hoat-hinh...
    // poster_url là relative → dùng .fullPosterUrl()
    @GET("v1/api/danh-sach/{type}")
    suspend fun getMovieList(
        @Path("type")         type: String,
        @Query("page")        page: Int = 1,
        @Query("sort_field")  sortField: String = "modified.time",
        @Query("sort_type")   sortType: String = "desc",
        @Query("limit")       limit: Int = 24
    ): Response<ListWrapperResponse>

    // Tìm kiếm
    @GET("v1/api/tim-kiem")
    suspend fun searchMovies(
        @Query("keyword") keyword: String,
        @Query("page")    page: Int = 1,
        @Query("limit")   limit: Int = 24
    ): Response<ListWrapperResponse>

    // Danh sách thể loại — trả về bare array List<GenreItem>
    @GET("the-loai")
    suspend fun getCategories(): Response<List<GenreItem>>

    // Danh sách quốc gia — trả về bare array List<GenreItem>
    @GET("quoc-gia")
    suspend fun getCountries(): Response<List<GenreItem>>

    // Phim theo thể loại
    @GET("v1/api/the-loai/{slug}")
    suspend fun getMoviesByCategory(
        @Path("slug")   slug: String,
        @Query("page")  page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>

    // Phim theo quốc gia
    @GET("v1/api/quoc-gia/{slug}")
    suspend fun getMoviesByCountry(
        @Path("slug")   slug: String,
        @Query("page")  page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>

    // Phim theo năm
    @GET("v1/api/nam/{year}")
    suspend fun getMoviesByYear(
        @Path("year")   year: Int,
        @Query("page")  page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://phimapi.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: PhimApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhimApiService::class.java)
    }
}
