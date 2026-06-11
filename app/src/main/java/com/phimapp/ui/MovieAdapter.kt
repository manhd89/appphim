package com.phimapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.phimapp.R
import com.phimapp.model.MovieItem

class MovieAdapter(
    private val onItemClick: (MovieItem) -> Unit,
    private val layoutType: LayoutType = LayoutType.GRID
) : ListAdapter<MovieItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    enum class LayoutType { GRID, HORIZONTAL, BANNER }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(a: MovieItem, b: MovieItem) = a.id == b.id
            override fun areContentsTheSame(a: MovieItem, b: MovieItem) = a == b
        }
    }

    // ── ViewHolders ──────────────────────────────────────────────────────────

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster:  ImageView = view.findViewById(R.id.imgPoster)
        val name:    TextView  = view.findViewById(R.id.tvName)
        val episode: TextView  = view.findViewById(R.id.tvEpisode)
        val quality: TextView  = view.findViewById(R.id.tvQuality)
        init { view.setOnClickListener { click() } }
    }

    inner class HorizontalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.imgPoster)
        val name:   TextView  = view.findViewById(R.id.tvName)
        val meta:   TextView  = view.findViewById(R.id.tvMeta)
        init { view.setOnClickListener { click() } }
    }

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumb:      ImageView = view.findViewById(R.id.imgThumb)
        val name:       TextView  = view.findViewById(R.id.tvName)
        val episode:    TextView  = view.findViewById(R.id.tvEpisode)
        val lang:       TextView  = view.findViewById(R.id.tvLang)
        val categories: TextView  = view.findViewById(R.id.tvCategories)
        init { view.setOnClickListener { click() } }
    }

    private fun RecyclerView.ViewHolder.click() {
        val pos = adapterPosition
        if (pos != RecyclerView.NO_POSITION) onItemClick(getItem(pos))
    }

    // ── Adapter overrides ────────────────────────────────────────────────────

    override fun getItemViewType(position: Int) = layoutType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (LayoutType.values()[viewType]) {
            LayoutType.GRID       -> GridViewHolder(inf.inflate(R.layout.item_movie_grid, parent, false))
            LayoutType.HORIZONTAL -> HorizontalViewHolder(inf.inflate(R.layout.item_movie_horizontal, parent, false))
            LayoutType.BANNER     -> BannerViewHolder(inf.inflate(R.layout.item_movie_banner, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is GridViewHolder       -> bindGrid(holder, item)
            is HorizontalViewHolder -> bindHorizontal(holder, item)
            is BannerViewHolder     -> bindBanner(holder, item)
        }
    }

    // ── Bind helpers — dùng fullPosterUrl() để luôn có domain đầy đủ ─────────

    private fun bindGrid(h: GridViewHolder, item: MovieItem) {
        h.name.text    = item.name
        h.episode.text = item.episodeCurrent ?: item.year?.toString() ?: ""
        h.quality.text = item.quality ?: "HD"
        Glide.with(h.poster)
            .load(item.fullPosterUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_poster)
            .into(h.poster)
    }

    private fun bindHorizontal(h: HorizontalViewHolder, item: MovieItem) {
        h.name.text = item.name
        val year = item.year?.toString() ?: ""
        val lang = item.lang ?: ""
        h.meta.text = listOf(year, lang).filter { it.isNotEmpty() }.joinToString(" · ")
        Glide.with(h.poster)
            .load(item.fullPosterUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_poster)
            .into(h.poster)
    }

    private fun bindBanner(h: BannerViewHolder, item: MovieItem) {
        h.name.text       = item.name
        h.episode.text    = item.episodeCurrent ?: "Full"
        h.lang.text       = item.lang ?: ""
        h.categories.text = item.category?.take(3)?.joinToString(" · ") { it.name } ?: ""
        Glide.with(h.thumb)
            .load(item.fullThumbUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_thumb)
            .into(h.thumb)
    }
}
