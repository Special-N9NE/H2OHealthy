package org.n9ne.profile.ui.adpter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuCompat
import androidx.recyclerview.widget.RecyclerView
import me.saket.cascade.CascadePopupMenu
import org.n9ne.common.R.color
import org.n9ne.common.R.drawable
import org.n9ne.common.R.string
import org.n9ne.common.model.Cup
import org.n9ne.common.util.interfaces.CupEditListener
import org.n9ne.profile.databinding.ItemCupBinding

class AddCupAdapter(
    private val list: List<Cup>,
    private val listener: CupEditListener
) : RecyclerView.Adapter<AddCupAdapter.ViewHolder>() {

    lateinit var context: Context

    inner class ViewHolder(private val b: ItemCupBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun setData(item: Cup) {
            b.item = item
            b.ivCup.setColorFilter(
                Color.parseColor(item.color),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            b.tvCapacity.text = item.capacity.toString()
            item.setupClick()
        }

        private fun Cup.setupClick() {
            b.clRoot.setOnClickListener {
                openPopup(this)
            }
        }

        private fun openPopup(item: Cup) {
            val popup = CascadePopupMenu(context, b.tvName)

            popup.setMenu()

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener,
                androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                    return if (menuItem == null)
                        false
                    else {
                        if (menuItem.title.toString() == context.getString(string.edit))
                            listener.onEdit(item, true)
                        else
                            listener.onEdit(item, false)

                        true
                    }
                }
            })
            popup.show()
        }

        private fun CascadePopupMenu.setMenu() {
            this.menu.apply {
                MenuCompat.setGroupDividerEnabled(this, true)

                val colorStateList = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_enabled)),
                    intArrayOf(
                        ResourcesCompat.getColor(
                            context.resources,
                            color.red,
                            context.theme
                        )
                    )
                )

                add(context.getString(string.edit)).setIcon(drawable.ic_edit)
                add(context.getString(string.remove)).setIcon(drawable.ic_close).iconTintList = colorStateList
            }
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