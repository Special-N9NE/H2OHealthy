package org.n9ne.h2ohealthy.ui.home.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.ItemCupBinding

class CupsAdapter(
    private val list: List<Cup>,
) : RecyclerView.Adapter<CupsAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemCupBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Cup) {
            b.item = item
            b.ivCup.setColorFilter(
                ContextCompat.getColor(context, item.color),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            b.tvCapacity.text = item.capacity.toString()
            setupClick(item)
        }

        private fun setupClick(item: Cup) {
            //TODO select item
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: ItemCupBinding =
            ItemCupBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.setData(item)
    }
}