package com.petitpre.easybelote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentNewGameBinding
import com.petitpre.easybelote.easyBelote


class NewGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val gameViewModel: NewGameViewModel = ViewModelProviders
            .of(this, NewGameViewModelFactory(requireContext().easyBelote.gameRepository))
            .get(NewGameViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentNewGameBinding>(
            inflater, R.layout.fragment_new_game, container, false
        ).apply {
            viewModel = gameViewModel
            setLifecycleOwner(viewLifecycleOwner)

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
            startGame.setOnClickListener {
                gameViewModel.startGame(it.findNavController())
            }
        }

        return binding.root
    }


}
