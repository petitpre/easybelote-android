package com.petitpre.easybelote.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
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
import android.view.inputmethod.EditorInfo


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
                ViewModelFactory { GameRoundViewModel(requireContext().easyBelote.gameRepository, gameId, roundId) }
            )
            .get(GameRoundViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentGameRoundBinding>(
            inflater, R.layout.fragment_game_round, container, false
        ).apply {
            viewModel = playingViewModel
            lifecycleOwner = viewLifecycleOwner

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            validate.setOnClickListener {
                playingViewModel.updateScores { it.findNavController().navigateUp() }
            }

            myScore.setOnEditorActionListener({ v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_NEXT
                ) {
                    playingViewModel.setTeamScore(true, v.text.toString())
                }
                false
            })
            otherScore.setOnEditorActionListener({ v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_NEXT
                ) {
                    playingViewModel.setTeamScore(false, v.text.toString())
                }
                false
            })

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

            Declaration.values().forEach {
                createTag(myDeclarations, it, true)
                createTag(otherDeclarations, it, false)
            }

            playingViewModel.gameWithRound.observe(viewLifecycleOwner, Observer {
                myDeclarations.children.forEach { view ->
                    if (view is Chip && view.tag != Declaration.Capot && view.tag != Declaration.Belote) {
                        view.isVisible = it.game.declarations
                    }
                }
                otherDeclarations.children.forEach { view ->
                    if (view is Chip && view.tag != Declaration.Capot && view.tag != Declaration.Belote) {
                        view.isVisible = it.game.declarations
                    }
                }
            })

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