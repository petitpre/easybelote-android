package com.petitpre.easybelote.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentGameBinding
import com.petitpre.easybelote.easyBelote

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<GameFragmentArgs>().value.gameId

        val playingViewModel: GameViewModel = ViewModelProviders
            .of(
                this,
                ViewModelFactory { GameViewModel(requireContext().easyBelote.gameRepository, gameId) }
            )
            .get(GameViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        ).apply {
            viewModel = playingViewModel
            lifecycleOwner = viewLifecycleOwner

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            playground.setOnClickListener {
                val direction = GameFragmentDirections.actionPlayingFragmentToScoreFragment(gameId)
                it.findNavController().navigate(direction)
            }
            history.setOnClickListener {
                val direction = GameFragmentDirections.actionPlayingFragmentToHistoryFragment(gameId)
                it.findNavController().navigate(direction)
            }
        }
        return binding.root
    }
}