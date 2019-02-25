package com.petitpre.easybelote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentHistoryBinding
import com.petitpre.easybelote.databinding.ItemRoundBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.model.Round

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<PlayingFragmentArgs>().value.gameId

        val historyViewModel: HistoryViewModel = ViewModelProviders
            .of(
                this,
                ViewModelFactory({
                    HistoryViewModel(
                        requireContext().easyBelote.gameRepository,
                        gameId
                    )
                })
            )
            .get(HistoryViewModel::class.java)

        val myScoreAdapter = ScoreListAdapter(true)
        val opponentScoreAdapter = ScoreListAdapter(false)

        val binding = DataBindingUtil.inflate<FragmentHistoryBinding>(
            inflater, R.layout.fragment_history, container, false
        ).apply {
            viewModel = historyViewModel
            setLifecycleOwner(viewLifecycleOwner)

            this.myScoreList.adapter = myScoreAdapter
            this.opponentScoreList.adapter = opponentScoreAdapter

            close.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        historyViewModel.game.observe(viewLifecycleOwner, Observer { game ->
            myScoreAdapter.submitList(game.rounds)
            opponentScoreAdapter.submitList(game.rounds)
        })

        return binding.root
    }

}


class ScoreListAdapter(val myScore: Boolean) : ListAdapter<Round, ScoreListAdapter.ViewHolder>(RoundDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val round = getItem(position)
        holder.apply {
            bind(if (myScore) round.team1.score else round.team2.score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRoundBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class ViewHolder(
        private val binding: ItemRoundBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(score: Long) {
            binding.apply {
                this.score = score
                executePendingBindings()
            }
        }
    }
}

private class RoundDiffCallback : DiffUtil.ItemCallback<Round>() {

    override fun areItemsTheSame(oldItem: Round, newItem: Round): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Round, newItem: Round): Boolean {
        return oldItem == newItem
    }
}