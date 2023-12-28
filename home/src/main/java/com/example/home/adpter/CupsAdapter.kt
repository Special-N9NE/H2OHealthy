package com.example.home.adpter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.ItemCupDialogBinding
import org.n9ne.common.model.Cup
import org.n9ne.common.util.interfaces.CupClickListener

class CupsAdapter(
    private val list: List<Cup>, private val listener: CupClickListener
) : RecyclerView.Adapter<CupsAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemCupDialogBinding) : RecyclerView.ViewHolder(b.root) {
        fun setData(item: Cup) {
            b.item = item
            b.ivCup.setColorFilter(
                Color.parseColor(item.color), android.graphics.PorterDuff.Mode.MULTIPLY
            )
            b.tvCapacity.text = item.capacity.toString()
            setupClick(item)
        }

        private fun setupClick(item: Cup) {
            b.clRoot.setOnClickListener {
                listener.onClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: ItemCupDialogBinding = ItemCupDialogBinding.inflate(inflater, parent, false)
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