package de.dhbw.wikigame.highscore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.dhbw.wikigame.R
import de.dhbw.wikigame.databinding.ScoreItemBinding

class HighscoreAdapter(var scoreList: MutableList<Highscore>) : RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder>(){

    inner class HighscoreViewHolder(val binding: ScoreItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighscoreViewHolder {
        val binding: ScoreItemBinding = ScoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val pvh: HighscoreViewHolder = HighscoreViewHolder(binding)
        return pvh
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    override fun onBindViewHolder(holder: HighscoreViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val score: Highscore = scoreList.get(position)
        val index = position + 1
        holder.binding.tvIndex.setText(index.toString() + ".")
        holder.binding.tvName.setText(score.name)
        holder.binding.tvPoints.setText(score.score.toString())
        if(score.time) holder.binding.ivTime.setImageResource(R.drawable.ic_time) else holder.binding.ivTime.setImageResource(R.drawable.ic_no_time)
        if(score.difficulty) holder.binding.ivLevel.setImageResource(R.drawable.ic_heavy) else holder.binding.ivLevel.setImageResource(R.drawable.ic_easy)

    }
}

