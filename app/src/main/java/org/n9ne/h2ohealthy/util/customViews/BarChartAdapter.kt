package org.n9ne.h2ohealthy.util.customViews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.boyzdroizy.simpleandroidbarchart.utils.ProgressBarAnimation
import org.n9ne.h2ohealthy.databinding.ItemBarChartBinding

class BarChartAdapter(
    private val items: MutableList<Int>,
    private val max: Int,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBarChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items[position], position)
    }

    inner class ViewHolder(val b: ItemBarChartBinding) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: Int, position: Int) {
            b.progressBar.max = max
            b.chartValue.text = (position*2).toString()
            initProgressBar(b.progressBar, item)
        }

        private fun initProgressBar(processBar: ProgressBar, value: Int) {
            processBar.progress = 0
            processBar.max = max

            ProgressBarAnimation(processBar, PROGRESS_ANIMATION).apply {
                setProgress(value)
            }
        }
    }

    companion object {
        const val PROGRESS_ANIMATION = 2000L
    }
}