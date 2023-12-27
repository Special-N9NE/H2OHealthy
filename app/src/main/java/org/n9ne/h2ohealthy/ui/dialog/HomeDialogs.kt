package org.n9ne.h2ohealthy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import org.n9ne.common.R.color
import org.n9ne.common.model.Cup
import org.n9ne.common.util.interfaces.AddWaterListener
import org.n9ne.common.util.interfaces.RemoveActivityListener
import org.n9ne.h2ohealthy.databinding.DialogActivityOptionsBinding
import org.n9ne.h2ohealthy.databinding.DialogAddBinding

fun Activity.addWaterDialog(
    layoutInflater: LayoutInflater,
    selectedCup: Cup? = null,
    doneListener: AddWaterListener? = null,
    selectListener: View.OnClickListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogAddBinding.inflate(layoutInflater)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(binding.root)
    dialog.window?.setLayout(
        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)
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
        binding.tvCup.text = "Select Cup"
    } else {
        binding.etAmount.setText(selectedCup.capacity.toString())
        binding.cvCup.setCardBackgroundColor(blue)
        binding.tvCup.text = "Cup Selected"
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
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(binding.root)
    dialog.window?.setLayout(
        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)

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
