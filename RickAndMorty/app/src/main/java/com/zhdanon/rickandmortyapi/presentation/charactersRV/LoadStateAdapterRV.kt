package com.zhdanon.rickandmortyapi.presentation.charactersRV

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhdanon.rickandmortyapi.databinding.LoadStateBinding

typealias TryAgainAction = () -> Unit

class LoadStateAdapterRV(
    private val tryAgainAction: TryAgainAction
) : LoadStateAdapter<LoadStateAdapterRV.StateHolder>() {
    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> error("Что-то пошло не так...")
        LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onBindViewHolder(holder: StateHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadStateBinding.inflate(inflater, parent, false)
        return StateHolder(binding, null, tryAgainAction)
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    class StateHolder(
        private val binding: LoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val tryAgainAction: TryAgainAction
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRefresh.setOnClickListener { tryAgainAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            binding.tvErrorMessage.isVisible = loadState is LoadState.Error
            binding.btnRefresh.isVisible = loadState is LoadState.Error
            binding.progressbar.isVisible = loadState !is LoadState.Error

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
                binding.progressbar.isVisible = false
            } else {
                binding.progressbar.isVisible = loadState is LoadState.Loading
            }
        }
    }
}