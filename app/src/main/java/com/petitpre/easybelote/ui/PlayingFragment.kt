package com.petitpre.easybelote.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentPlayingBinding
import com.petitpre.easybelote.easyBelote

class PlayingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<PlayingFragmentArgs>().value.gameId

        val playingViewModel: PlayingViewModel = ViewModelProviders
            .of(
                this,
                ViewModelFactory({ PlayingViewModel(requireContext().easyBelote.gameRepository, gameId) })
            )
            .get(PlayingViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentPlayingBinding>(
            inflater, R.layout.fragment_playing, container, false
        ).apply {
            viewModel = playingViewModel
            setLifecycleOwner(viewLifecycleOwner)

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            playground.setOnClickListener {
                val direction = PlayingFragmentDirections.actionPlayingFragmentToScoreFragment(gameId)
                it.findNavController().navigate(direction)
            }
            history.setOnClickListener {
                val direction = PlayingFragmentDirections.actionPlayingFragmentToHistoryFragment(gameId)
                it.findNavController().navigate(direction)
            }
        }

        return binding.root
    }


}
