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

import com.petitpre.easybelote.databinding.FragmentScoreBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.R

class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<ScoreFragmentArgs>().value.gameId

        val playingViewModel: ScoreViewModel = ViewModelProviders
            .of(this, ViewModelFactory({ ScoreViewModel(requireContext().easyBelote.gameRepository, gameId)}))
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
        }
        return binding.root
    }

}
