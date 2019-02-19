package com.petitpre.easybelote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.petitpre.easybelote.R
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        view.start_game.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_start_game, null)
        )

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = StartFragment()
    }
}
