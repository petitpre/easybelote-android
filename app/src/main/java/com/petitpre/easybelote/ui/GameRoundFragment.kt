package com.petitpre.easybelote.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentGameRoundBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.model.Declaration
import com.petitpre.easybelote.utils.setOnDoneListener

class GameRoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = navArgs<GameRoundFragmentArgs>().value
        val gameId = args.gameId
        val roundId = args.roundId
        val bidding = args.bidding

        val playingViewModel: GameRoundViewModel = ViewModelProviders
            .of(
                this,
                ViewModelFactory {
                    GameRoundViewModel(
                        requireContext().easyBelote.gameRepository,
                        gameId,
                        roundId,
                        bidding
                    )
                }
            )
            .get(GameRoundViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentGameRoundBinding>(
            inflater, R.layout.fragment_game_round, container, false
        ).apply {
            viewModel = playingViewModel
            lifecycleOwner = viewLifecycleOwner

            close.setOnClickListener { it.findNavController().navigateUp() }
            validate.setOnClickListener { playingViewModel.updateScores { it.findNavController().navigateUp() } }

            myScore.setOnDoneListener { playingViewModel.setTeamScore(true, it.text.toString()) }
            otherScore.setOnDoneListener({ playingViewModel.setTeamScore(false, it.text.toString()) })

            fun createChip(parent: ViewGroup, team: Boolean, declaration: LiveData<AvailableDeclaration>): Chip {
                val chip = (inflater.inflate(R.layout.declaration, parent, false) as Chip)
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    playingViewModel.setDeclaration(team, buttonView.tag as Declaration, isChecked)
                }
                declaration.observe(viewLifecycleOwner, Observer { d ->
                    chip.setText(d.declaration.text)
                    chip.tag = d.declaration
                })
                parent.addView(chip)
                return chip
            }

            playingViewModel.team1Declaration.forEach { declaration ->
                createChip(myDeclarations, true, declaration)
            }
            playingViewModel.team2Declaration.forEach { declaration ->
                createChip(otherDeclarations, false, declaration)
            }
        }
        return binding.root
    }
}