package com.zhdanon.rickandmortyapi.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zhdanon.rickandmortyapi.databinding.FragmentCurrentCharacterBinding

class FragmentCurrentCharacter : Fragment() {
    private var _binding: FragmentCurrentCharacterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentCharacterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentCurrentCharacterArgs by navArgs()

        (activity as AppCompatActivity).supportActionBar?.title = args.currentCharacter.name

        Glide.with(requireView().context)
            .load(args.currentCharacter.image)
            .into(binding.currentAvatar)

        binding.currentCreated.text = args.currentCharacter.origin.name
        binding.currentGender.text = args.currentCharacter.gender
        binding.currentLocation.text = args.currentCharacter.location?.name

        val episodeList = StringBuilder()
        args.currentCharacter.episode.map { episode ->
            val lastIndex = episode.lastIndexOf('/')
            val temp = episode.removeRange(0, lastIndex + 1)
            if (episodeList.isBlank()) episodeList.append(temp) else episodeList.append(", $temp")
        }
        binding.currentEpisodes.text = episodeList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}