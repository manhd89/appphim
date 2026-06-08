package com.phimapp.ui.detail

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.phimapp.R
import com.phimapp.data.Result
import com.phimapp.databinding.FragmentMovieDetailBinding
import com.phimapp.model.Episode
import com.phimapp.model.EpisodeServer
import com.phimapp.viewmodel.MovieDetailViewModel

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private var episodeServers: List<EpisodeServer> = emptyList()
    private var selectedServerIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slug = arguments?.getString("slug") ?: return
        viewModel.loadDetail(slug)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.detail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.contentGroup.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.contentGroup.visibility = View.VISIBLE
                    val movie = result.data.movie ?: return@observe
                    episodeServers = result.data.episodes ?: emptyList()
                    displayMovieInfo(result.data)
                    setupEpisodes()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = result.message
                }
            }
        }
    }

    private fun displayMovieInfo(data: com.phimapp.model.MovieDetailResponse) {
        val movie = data.movie ?: return

        // Backdrop
        Glide.with(this)
            .load(movie.thumbUrl)
            .into(binding.imgBackdrop)

        // Poster
        Glide.with(this)
            .load(movie.posterUrl)
            .into(binding.imgPoster)

        binding.tvTitle.text = movie.name
        binding.tvOriginTitle.text = movie.originName ?: ""
        binding.tvYear.text = movie.year?.toString() ?: ""
        binding.tvQuality.text = movie.quality ?: "HD"
        binding.tvLang.text = movie.lang ?: ""
        binding.tvTime.text = movie.time ?: ""
        binding.tvEpisode.text = "${movie.episodeCurrent ?: ""} / ${movie.episodeTotal ?: "?"}"
        binding.tvStatus.text = when (movie.status) {
            "completed" -> "Hoàn tất"
            "ongoing" -> "Đang chiếu"
            else -> movie.status ?: ""
        }

        val content = movie.content ?: "Chưa có mô tả"
        binding.tvContent.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.tvCategories.text = movie.category?.joinToString(" · ") { it.name } ?: ""
        binding.tvCountry.text = movie.country?.joinToString(", ") { it.name } ?: ""
        binding.tvActors.text = movie.actor?.take(5)?.joinToString(", ") ?: "Chưa có thông tin"
        binding.tvDirector.text = movie.director?.joinToString(", ") ?: "Chưa có thông tin"
    }

    private fun setupEpisodes() {
        if (episodeServers.isEmpty()) {
            binding.episodeSection.visibility = View.GONE
            return
        }
        binding.episodeSection.visibility = View.VISIBLE

        // Server tabs
        binding.serverTabLayout.removeAllViews()
        episodeServers.forEachIndexed { index, server ->
            val tab = layoutInflater.inflate(R.layout.item_server_tab, binding.serverTabLayout, false) as TextView
            tab.text = server.serverName
            tab.isSelected = index == selectedServerIndex
            tab.setOnClickListener {
                selectedServerIndex = index
                setupEpisodes()
            }
            binding.serverTabLayout.addView(tab)
        }

        // Episodes grid
        val currentServer = episodeServers.getOrNull(selectedServerIndex) ?: return
        binding.episodeGrid.removeAllViews()
        currentServer.serverData.forEachIndexed { index, episode ->
            val epView = layoutInflater.inflate(R.layout.item_episode_chip, binding.episodeGrid, false) as TextView
            epView.text = episode.name
            epView.setOnClickListener { playEpisode(episode) }
            binding.episodeGrid.addView(epView)
        }

        // Play first episode button
        val firstEp = currentServer.serverData.firstOrNull()
        if (firstEp != null) {
            binding.btnPlay.setOnClickListener { playEpisode(firstEp) }
        }
    }

    private fun playEpisode(episode: Episode) {
        val link = episode.linkM3u8 ?: episode.linkEmbed ?: return
        findNavController().navigate(R.id.action_detail_to_player,
            Bundle().apply {
                putString("link", link)
                putString("title", episode.filename ?: episode.name)
                putBoolean("isM3u8", episode.linkM3u8 != null)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
