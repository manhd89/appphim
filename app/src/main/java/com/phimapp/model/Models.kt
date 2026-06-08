package com.phimapp.model

import com.google.gson.annotations.SerializedName

// ---- List response ----
data class MovieListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("msg") val msg: String,
    @SerializedName("items") val items: List<MovieItem>,
    @SerializedName("pagination") val pagination: Pagination?
)

data class MovieItem(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("origin_name") val originName: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("poster_url") val posterUrl: String?,
    @SerializedName("thumb_url") val thumbUrl: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("quality") val quality: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("episode_current") val episodeCurrent: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("category") val category: List<Category>?,
    @SerializedName("country") val country: List<Country>?,
    @SerializedName("tmdb") val tmdb: Tmdb?
)

data class Pagination(
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("totalItemsPerPage") val totalItemsPerPage: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int
)

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class Country(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class Tmdb(
    @SerializedName("type") val type: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("vote_average") val voteAverage: Double?
)

// ---- Detail response ----
data class MovieDetailResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("movie") val movie: MovieDetail?,
    @SerializedName("episodes") val episodes: List<EpisodeServer>?
)

data class MovieDetail(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("origin_name") val originName: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("poster_url") val posterUrl: String?,
    @SerializedName("thumb_url") val thumbUrl: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("quality") val quality: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("episode_current") val episodeCurrent: String?,
    @SerializedName("episode_total") val episodeTotal: String?,
    @SerializedName("category") val category: List<Category>?,
    @SerializedName("country") val country: List<Country>?,
    @SerializedName("actor") val actor: List<String>?,
    @SerializedName("director") val director: List<String>?,
    @SerializedName("tmdb") val tmdb: Tmdb?
)

data class EpisodeServer(
    @SerializedName("server_name") val serverName: String,
    @SerializedName("server_data") val serverData: List<Episode>
)

data class Episode(
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("filename") val filename: String?,
    @SerializedName("link_embed") val linkEmbed: String?,
    @SerializedName("link_m3u8") val linkM3u8: String?
)

// ---- Category/Country list ----
data class GenreItem(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

// ---- Search response ----
data class SearchResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: SearchData?
)

data class SearchData(
    @SerializedName("items") val items: List<MovieItem>,
    @SerializedName("params") val params: SearchParams?
)

data class SearchParams(
    @SerializedName("pagination") val pagination: Pagination?
)
