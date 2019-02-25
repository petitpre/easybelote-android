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
import com.google.android.material.chip.Chip

import com.petitpre.easybelote.databinding.FragmentScoreBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.R
import com.petitpre.easybelote.model.Declaration

class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<ScoreFragmentArgs>().value.gameId

        val playingViewModel: ScoreViewModel = ViewModelProviders
            .of(this, ViewModelFactory({ ScoreViewModel(requireContext().easyBelote.gameRepository, gameId) }))
            .get(ScoreViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentScoreBinding>(
            inflater, R.layout.fragment_score, container, false
        ).apply {
            viewModel = playingViewModel
            setLifecycleOwner(viewLifecycleOwner)

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            validate.setOnClickListener {
                playingViewModel.updateScores(it.findNavController())
            }

            Declaration.values().forEach { declaration ->
               val chip = (inflater.inflate(R.layout.declaration, myDeclarations, false) as Chip).apply {
                    this.setText(declaration.text)

                    this.setOnCheckedChangeListener { buttonView, isChecked ->
                        playingViewModel.addDeclaration(true, declaration)
                    }
                }
                myDeclarations.addView(chip)


                val otherchip = (inflater.inflate(R.layout.declaration, otherDeclarations, false) as Chip).apply {
                    this.setText(declaration.text)

                    this.setOnCheckedChangeListener { buttonView, isChecked ->
                        playingViewModel.addDeclaration(false, declaration)
                    }
                }
                otherDeclarations.addView(otherchip)
            }
        }
        return binding.root
    }
}