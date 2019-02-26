package com.petitpre.easybelote.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentGameRoundBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.model.Declaration

class GameRoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = navArgs<GameRoundFragmentArgs>().value
        val gameId = args.gameId
        val roundId = args.roundId

        val playingViewModel: GameRoundViewModel = ViewModelProviders
            .of(
                this,
                ViewModelFactory({ GameRoundViewModel(requireContext().easyBelote.gameRepository, gameId, roundId) })
            )
            .get(GameRoundViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentGameRoundBinding>(
            inflater, R.layout.fragment_game_round, container, false
        ).apply {
            viewModel = playingViewModel
            setLifecycleOwner(viewLifecycleOwner)

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            validate.setOnClickListener {
                playingViewModel.updateScores { it.findNavController().navigateUp() }
            }

            fun createTag(parent: ViewGroup, declaration: Declaration, myTeam: Boolean) {
                val chip = (inflater.inflate(R.layout.declaration, parent, false) as Chip).apply {
                    this.setText(declaration.text)
                    this.tag = declaration
                    this.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked)
                            playingViewModel.addDeclaration(myTeam, declaration)
                        else
                            playingViewModel.removeDeclaration(myTeam, declaration)
                    }
                }
                parent.addView(chip)
            }

            Declaration.values().forEach { declaration ->
                createTag(myDeclarations, declaration, true)
                createTag(otherDeclarations, declaration, false)
            }

            playingViewModel.round.observe(viewLifecycleOwner, Observer { round ->
                myDeclarations.children.forEach { view ->
                    if (view is Chip) {
                        view.isChecked = round.team1.declarations.contains(view.tag)
                    }
                }
                otherDeclarations.children.forEach { view ->
                    if (view is Chip) {
                        view.isChecked = round.team2.declarations.contains(view.tag)
                    }
                }
            })
        }
        return binding.root
    }
}