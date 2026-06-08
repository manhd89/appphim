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
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem) = oldItem == newItem
        }
    }

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.imgPoster)
        val name: TextView = view.findViewById(R.id.tvName)
        val episode: TextView = view.findViewById(R.id.tvEpisode)
        val quality: TextView = view.findViewById(R.id.tvQuality)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_ID) onItemClick(getItem(pos))
            }
        }
    }

    inner class HorizontalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.imgPoster)
        val name: TextView = view.findViewById(R.id.tvName)
        val meta: TextView = view.findViewById(R.id.tvMeta)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_ID) onItemClick(getItem(pos))
            }
        }
    }

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumb: ImageView = view.findViewById(R.id.imgThumb)
        val name: TextView = view.findViewById(R.id.tvName)
        val episode: TextView = view.findViewById(R.id.tvEpisode)
        val lang: TextView = view.findViewById(R.id.tvLang)
        val categories: TextView = view.findViewById(R.id.tvCategories)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_ID) onItemClick(getItem(pos))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = layoutType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (LayoutType.values()[viewType]) {
            LayoutType.GRID -> GridViewHolder(inflater.inflate(R.layout.item_movie_grid, parent, false))
            LayoutType.HORIZONTAL -> HorizontalViewHolder(inflater.inflate(R.layout.item_movie_horizontal, parent, false))
            LayoutType.BANNER -> BannerViewHolder(inflater.inflate(R.layout.item_movie_banner, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is GridViewHolder -> bindGrid(holder, item)
            is HorizontalViewHolder -> bindHorizontal(holder, item)
            is BannerViewHolder -> bindBanner(holder, item)
        }
    }

    private fun bindGrid(holder: GridViewHolder, item: MovieItem) {
        holder.name.text = item.name
        holder.episode.text = item.episodeCurrent ?: item.year?.toString() ?: ""
        holder.quality.text = item.quality ?: "HD"
        Glide.with(holder.poster)
            .load(item.posterUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_poster)
            .into(holder.poster)
    }

    private fun bindHorizontal(holder: HorizontalViewHolder, item: MovieItem) {
        holder.name.text = item.name
        val year = item.year?.toString() ?: ""
        val lang = item.lang ?: ""
        holder.meta.text = if (year.isNotEmpty() && lang.isNotEmpty()) "$year · $lang" else year + lang
        Glide.with(holder.poster)
            .load(item.posterUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_poster)
            .into(holder.poster)
    }

    private fun bindBanner(holder: BannerViewHolder, item: MovieItem) {
        holder.name.text = item.name
        holder.episode.text = item.episodeCurrent ?: "Full"
        holder.lang.text = item.lang ?: ""
        holder.categories.text = item.category?.take(3)?.joinToString(" · ") { it.name } ?: ""
        Glide.with(holder.thumb)
            .load(item.thumbUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_thumb)
            .into(holder.thumb)
    }
}
