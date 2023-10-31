package org.n9ne.h2ohealthy.ui.profile.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Member
import org.n9ne.h2ohealthy.databinding.ItemMemberBinding
import org.n9ne.h2ohealthy.util.setGradient

class MemberAdapter(
    private val list: List<Member>
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemMemberBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Member, position: Int) {
            b.item = item

            b.tvNumber.setGradient(context, R.color.linearPurpleStart, R.color.linearPurpleEnd)
            b.tvScore.setGradient(context, R.color.linearPurpleStart, R.color.linearPurpleEnd)

            b.tvNumber.text = (position + 1).toString()
            b.tvScore.text = item.score.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: ItemMemberBinding =
            ItemMemberBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.setData(item, position)
    }
}