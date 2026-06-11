package com.phimapp.model

import com.google.gson.annotations.SerializedName

const val CDN_IMAGE_BASE = "https://phimimg.com/"

// helper: đảm bảo URL luôn có domain đầy đủ
fun String?.toFullImageUrl(): String? {
    if (this.isNullOrBlank()) return null
    return if (this.startsWith("http")) this else "$CDN_IMAGE_BASE$this"
}

// ─── /danh-sach/phim-moi-cap-nhat-v3 ────────────────────────────────────────
// { status, msg, items: [...], pagination: {...} }
data class MovieListResponse(
    @SerializedName("status")     val status: Boolean,
    @SerializedName("msg")        val msg: String?,
    @SerializedName("items")      val items: List<MovieItem>,
    @SerializedName("pagination") val pagination: Pagination?
)

// ─── /v1/api/danh-sach | the-loai | quoc-gia | tim-kiem | nam ───────────────
// { status, msg, data: { items, params, APP_DOMAIN_CDN_IMAGE } }
data class ListWrapperResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("msg")    val msg: String?,
    @SerializedName("data")   val data: ListData?
)

data class ListData(
    @SerializedName("items")               val items: List<MovieItem>,
    @SerializedName("params")              val params: ListParams?,
    @SerializedName("APP_DOMAIN_CDN_IMAGE") val cdnImageBase: String?
)

data class ListParams(
    @SerializedName("pagination") val pagination: Pagination?
)

data class Pagination(
    @SerializedName("totalItems")        val totalItems: Int,
    @SerializedName("totalItemsPerPage") val totalItemsPerPage: Int,
    @SerializedName("currentPage")       val currentPage: Int,
    @SerializedName("totalPages")        val totalPages: Int
)

// ─── Movie item — dùng chung cho mọi danh sách ──────────────────────────────
data class MovieItem(
    @SerializedName("_id")             val id: String,
    @SerializedName("name")            val name: String,
    @SerializedName("slug")            val slug: String,
    @SerializedName("origin_name")     val originName: String?,
    @SerializedName("type")            val type: String?,
    @SerializedName("poster_url")      val posterUrl: String?,
    @SerializedName("thumb_url")       val thumbUrl: String?,
    @SerializedName("year")            val year: Int?,
    @SerializedName("quality")         val quality: String?,
    @SerializedName("lang")            val lang: String?,
    @SerializedName("episode_current") val episodeCurrent: String?,
    @SerializedName("time")            val time: String?,
    @SerializedName("category")        val category: List<Category>?,
    @SerializedName("country")         val country: List<Country>?,
    @SerializedName("tmdb")            val tmdb: Tmdb?
) {
    // poster_url từ /v1/api/* là relative path, từ /danh-sach/v3 là full URL
    fun fullPosterUrl(): String? = posterUrl.toFullImageUrl()
    fun fullThumbUrl(): String?  = thumbUrl.toFullImageUrl()
}

data class Category(
    @SerializedName("id")   val id: String?,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class Country(
    @SerializedName("id")   val id: String?,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class Tmdb(
    @SerializedName("type")         val type: String?,
    @SerializedName("id")           val id: String?,
    @SerializedName("vote_average") val voteAverage: Double?
)

// ─── /phim/{slug} ────────────────────────────────────────────────────────────
// { status, msg, movie: {...}, episodes: [...] }
data class MovieDetailResponse(
    @SerializedName("status")   val status: Boolean,
    @SerializedName("msg")      val msg: String?,
    @SerializedName("movie")    val movie: MovieDetail?,
    @SerializedName("episodes") val episodes: List<EpisodeServer>?
)

data class MovieDetail(
    @SerializedName("_id")             val id: String,
    @SerializedName("name")            val name: String,
    @SerializedName("slug")            val slug: String,
    @SerializedName("origin_name")     val originName: String?,
    @SerializedName("content")         val content: String?,
    @SerializedName("type")            val type: String?,
    @SerializedName("status")          val status: String?,
    @SerializedName("poster_url")      val posterUrl: String?,
    @SerializedName("thumb_url")       val thumbUrl: String?,
    @SerializedName("year")            val year: Int?,
    @SerializedName("quality")         val quality: String?,
    @SerializedName("lang")            val lang: String?,
    @SerializedName("time")            val time: String?,
    @SerializedName("episode_current") val episodeCurrent: String?,
    @SerializedName("episode_total")   val episodeTotal: String?,
    @SerializedName("category")        val category: List<Category>?,
    @SerializedName("country")         val country: List<Country>?,
    @SerializedName("actor")           val actor: List<String>?,
    @SerializedName("director")        val director: List<String>?,
    @SerializedName("tmdb")            val tmdb: Tmdb?
) {
    fun fullPosterUrl(): String? = posterUrl.toFullImageUrl()
    fun fullThumbUrl(): String?  = thumbUrl.toFullImageUrl()
}

data class EpisodeServer(
    @SerializedName("server_name") val serverName: String,
    @SerializedName("server_data") val serverData: List<Episode>
)

data class Episode(
    @SerializedName("name")       val name: String,
    @SerializedName("slug")       val slug: String,
    @SerializedName("filename")   val filename: String?,
    @SerializedName("link_embed") val linkEmbed: String?,
    @SerializedName("link_m3u8")  val linkM3u8: String?
)

// ─── /the-loai  /quoc-gia — trả về bare array ────────────────────────────────
data class GenreItem(
    @SerializedName("_id")  val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)
