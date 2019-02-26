package com.petitpre.easybelote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petitpre.easybelote.R
import com.petitpre.easybelote.databinding.FragmentHistoryBinding
import com.petitpre.easybelote.databinding.ItemRoundBinding
import com.petitpre.easybelote.easyBelote
import com.petitpre.easybelote.model.Round
import com.petitpre.easybelote.model.TeamScore

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameId = navArgs<HistoryFragmentArgs>().value.gameId

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

        val myScoreAdapter = ScoreListAdapter({ round ->
            val direction =
                HistoryFragmentDirections.actionHistoryFragmentToScoreFragment(round.gameId).setRoundId(round.id)
            findNavController().navigate(direction)
        })
        val opponentScoreAdapter = ScoreListAdapter({ round ->
            val direction =
                HistoryFragmentDirections.actionHistoryFragmentToScoreFragment(round.gameId).setRoundId(round.id)
            findNavController().navigate(direction)
        })

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

        historyViewModel.gameWithRound.observe(viewLifecycleOwner, Observer { game ->
            myScoreAdapter.submitList(game.rounds.map { Pair(it, it.team1) })
            opponentScoreAdapter.submitList(game.rounds.map { Pair(it, it.team2) })
        })

        return binding.root
    }

}


class ScoreListAdapter(val clickHandler: (Round) -> Unit) :
    ListAdapter<Pair<Round, TeamScore>, ScoreListAdapter.ViewHolder>(TeamScoreDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = getItem(position)
        holder.apply {
            bind(score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRoundBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            clickHandler
        )
    }

    class ViewHolder(
        private val binding: ItemRoundBinding,
        private val clickHandler: (Round) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: Pair<Round, TeamScore>) {
            binding.apply {
                this.score = entry.second.totalScore
                executePendingBindings()
                item.setOnClickListener {
                    clickHandler(entry.first)
                }
            }
        }
    }
}

private class TeamScoreDiffCallback : DiffUtil.ItemCallback<Pair<Round, TeamScore>>() {

    override fun areItemsTheSame(oldItem: Pair<Round, TeamScore>, newItem: Pair<Round, TeamScore>): Boolean {
        return oldItem.first.id == newItem.first.id
    }

    override fun areContentsTheSame(oldItem: Pair<Round, TeamScore>, newItem: Pair<Round, TeamScore>): Boolean {
        return oldItem == newItem
    }
}