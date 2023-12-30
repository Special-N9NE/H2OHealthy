package org.n9ne.profile.ui.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.n9ne.common.model.Image
import org.n9ne.common.util.interfaces.AvatarClickListener
import org.n9ne.profile.databinding.ItemAvatarBinding

class AvatarAdapter(
    private val list: List<Image>,
    private val listener: AvatarClickListener
) : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemAvatarBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Image) {
            b.iv.setImageResource(item.res)
            item.setupClick()
        }

        private fun Image.setupClick() {
            b.root.setOnClickListener {
                listener.onClick(this.title)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: ItemAvatarBinding =
            ItemAvatarBinding.inflate(inflater, parent, false)
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