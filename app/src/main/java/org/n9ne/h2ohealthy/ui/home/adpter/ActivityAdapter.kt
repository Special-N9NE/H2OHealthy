package org.n9ne.h2ohealthy.ui.home.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.databinding.ItemActiviyBinding
import org.n9ne.h2ohealthy.util.interfaces.MenuClickListener

class ActivityAdapter(
    private val list: List<Activity>,
    private val listener: MenuClickListener
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    inner class ViewHolder(private val b: ItemActiviyBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Activity) {
            b.tvAmount.text = item.amount
            b.tvTime.text = item.time

            setupClick(item)
        }

        private fun setupClick(item: Activity) {
            b.ivMenu.setOnClickListener {
                listener.onMenuClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemActiviyBinding =
            ItemActiviyBinding.inflate(inflater, parent, false)
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