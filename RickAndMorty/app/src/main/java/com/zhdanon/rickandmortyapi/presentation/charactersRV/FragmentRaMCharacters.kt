package com.zhdanon.rickandmortyapi.presentation.charactersRV

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhdanon.rickandmortyapi.R
import com.zhdanon.rickandmortyapi.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortyapi.databinding.FragmentCharactersListRvBinding
import com.zhdanon.rickandmortyapi.presentation.DialogFilter
import com.zhdanon.rickandmortyapi.presentation.RaMAdapterRV
import com.zhdanon.rickandmortyapi.presentation.RaMViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentRaMCharacters : Fragment() {
    private var _binding: FragmentCharactersListRvBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuHost: MenuHost

    private val viewModel: RaMViewModel by activityViewModels()
//    private lateinit var pagedAdapter: RaMAdapterRV

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersListRvBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка custom OptionMenu
        setFilterMenu()

        // установка адаптера RecyclerView
        setRecyclerAdapter()

        // получение списка персонажей
        getCharactersList()

        // обработка состояний загрузки списка персонажей
        stateLoadingListener()

        binding.swipeRefresh.setOnRefreshListener { viewModel.pagedAdapter.refresh() }
        binding.rvBtnRefresh.setOnClickListener { viewModel.pagedAdapter.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerAdapter() {
        viewModel.pagedAdapter = RaMAdapterRV { onItemClick(it) }
        binding.charactersList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val tryAgainAction = { viewModel.pagedAdapter.retry() }
        binding.charactersList.adapter =
            viewModel.pagedAdapter.withLoadStateFooter(LoadStateAdapterRV(tryAgainAction))
        (binding.charactersList.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false
    }

    // Listener для элемента RecyclerView
    private fun onItemClick(character: ResultCharacterDto) {
        val action =
            FragmentRaMCharactersDirections.actionFragmentRickAndMortyCharactersToFragmentCurrentCharacter(
                character
            )
        findNavController().navigate(action)
    }

    private fun getCharactersList() {
        viewModel.pagedCharacters.onEach {
            viewModel.pagedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun stateLoadingListener() {
        viewModel.pagedAdapter.loadStateFlow.onEach { state ->
            val currentState = state.refresh
            binding.swipeRefresh.isRefreshing = currentState == LoadState.Loading
            binding.rvProgressbar.isVisible = currentState == LoadState.Loading
            when (currentState) {
                is LoadState.Error -> {
                    binding.charactersList.visibility = View.GONE
                    binding.loadState.visibility = View.VISIBLE
                }
                else -> {
                    binding.charactersList.visibility = View.VISIBLE
                    binding.loadState.visibility = View.GONE
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setFilterMenu() {
        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_filter, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_filter -> {
                        val fm = childFragmentManager
                        val filterDialog = DialogFilter()
                        filterDialog.show(fm, DialogFilter.SHOW_FILTER_DIALOG)
                    }
                    R.id.action_clear_filter -> {
                        viewModel.pagedAdapter.refresh()
                        viewModel.setFilterParams(
                            status = "",
                            gender = ""
                        )
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
    }
}