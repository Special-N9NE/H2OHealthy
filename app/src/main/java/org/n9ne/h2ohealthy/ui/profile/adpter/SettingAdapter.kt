package org.n9ne.h2ohealthy.ui.profile.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.databinding.ItemSettingBinding
import org.n9ne.h2ohealthy.util.interfaces.SettingClickListener

class SettingAdapter(
    private val list: List<Setting>,
    private val listener: SettingClickListener
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemSettingBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Setting) {
            b.item = item
            setupClick(item)
        }

        private fun setupClick(item: Setting) {
            b.clRoot.setOnClickListener {
                listener.settingClicked(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: ItemSettingBinding =
            ItemSettingBinding.inflate(inflater, parent, false)
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