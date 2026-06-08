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

    // Phim mới cập nhật V3
    @GET("danh-sach/phim-moi-cap-nhat-v3")
    suspend fun getNewMovies(@Query("page") page: Int = 1): Response<MovieListResponse>

    // Chi tiết phim
    @GET("phim/{slug}")
    suspend fun getMovieDetail(@Path("slug") slug: String): Response<MovieDetailResponse>

    // Danh sách tổng hợp
    @GET("v1/api/danh-sach/{type}")
    suspend fun getMovieList(
        @Path("type") type: String,
        @Query("page") page: Int = 1,
        @Query("sort_field") sortField: String = "modified.time",
        @Query("sort_type") sortType: String = "desc",
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>

    // Tìm kiếm
    @GET("v1/api/tim-kiem")
    suspend fun searchMovies(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<SearchResponse>

    // Thể loại list
    @GET("the-loai")
    suspend fun getCategories(): Response<List<GenreItem>>

    // Quốc gia list
    @GET("quoc-gia")
    suspend fun getCountries(): Response<List<GenreItem>>

    // Phim theo thể loại
    @GET("v1/api/the-loai/{slug}")
    suspend fun getMoviesByCategory(
        @Path("slug") slug: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>

    // Phim theo quốc gia
    @GET("v1/api/quoc-gia/{slug}")
    suspend fun getMoviesByCountry(
        @Path("slug") slug: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 24
    ): Response<ListWrapperResponse>
}

data class ListWrapperResponse(
    @com.google.gson.annotations.SerializedName("status") val status: Boolean,
    @com.google.gson.annotations.SerializedName("msg") val msg: String?,
    @com.google.gson.annotations.SerializedName("data") val data: ListData?
)

data class ListData(
    @com.google.gson.annotations.SerializedName("items") val items: List<com.phimapp.model.MovieItem>,
    @com.google.gson.annotations.SerializedName("params") val params: ListParams?
)

data class ListParams(
    @com.google.gson.annotations.SerializedName("pagination") val pagination: com.phimapp.model.Pagination?
)

object RetrofitClient {
    private const val BASE_URL = "https://phimapi.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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
