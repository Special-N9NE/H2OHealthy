package org.n9ne.common.dialog

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import org.n9ne.common.R
import org.n9ne.common.R.color
import org.n9ne.common.databinding.DialogActivityOptionsBinding
import org.n9ne.common.databinding.DialogAddBinding
import org.n9ne.common.model.Cup
import org.n9ne.common.util.Utils.setDialog
import org.n9ne.common.util.interfaces.AddWaterListener
import org.n9ne.common.util.interfaces.RemoveActivityListener

fun Activity.addWaterDialog(
    layoutInflater: LayoutInflater,
    selectedCup: Cup? = null,
    doneListener: AddWaterListener? = null,
    selectListener: View.OnClickListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogAddBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.setDialog()

    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }
    binding.cvCup.setOnClickListener {
        selectListener.onClick(binding.cvCup)
        dialog.dismiss()
    }
    binding.bDone.setOnClickListener {
        doneListener?.let { click ->
            val amount = binding.etAmount.text.toString()
            if (amount.isNotEmpty()) {
                click.onAdd(amount)
                dialog.dismiss()
            }
        }
    }
    val blue = ResourcesCompat.getColor(this.resources, color.linearBlueEnd, this.theme)
    val white = ResourcesCompat.getColor(this.resources, color.white, this.theme)

    if (selectedCup == null) {
        binding.etAmount.setText("")
        binding.cvCup.setCardBackgroundColor(white)
        binding.tvCup.text = getString(R.string.select_cup)
    } else {
        binding.etAmount.setText(selectedCup.capacity.toString())
        binding.cvCup.setCardBackgroundColor(blue)
        binding.tvCup.text = getString(R.string.selected_cup)
    }

    return dialog
}

fun Activity.activityOptionDialog(
    item: org.n9ne.common.model.Activity,
    listener: AddWaterListener,
    removeListener: RemoveActivityListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogActivityOptionsBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.setDialog()

    binding.etAmount.setText(item.amount)
    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }
    binding.bSumbit.setOnClickListener {
        val value = binding.etAmount.text.toString()
        if (value.isNotEmpty()) {
            listener.onAdd(value)
            dialog.dismiss()
        }
    }
    binding.cvRemove.setOnClickListener {
        removeListener.onRemove()
        dialog.dismiss()
    }
    return dialog
}
