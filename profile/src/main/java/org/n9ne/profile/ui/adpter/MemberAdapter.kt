package org.n9ne.profile.ui.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.n9ne.common.R.color
import org.n9ne.common.model.Member
import org.n9ne.common.util.setGradient
import org.n9ne.common.util.setUserAvatar
import org.n9ne.profile.databinding.ItemMemberBinding

class MemberAdapter(
    private val list: List<Member>
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemMemberBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Member, position: Int) {
            b.item = item

            b.ivProfile.setUserAvatar(item.profile)

            b.tvNumber.setGradient(context, color.linearPurpleStart, color.linearPurpleEnd)
            b.tvScore.setGradient(context, color.linearPurpleStart, color.linearPurpleEnd)

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