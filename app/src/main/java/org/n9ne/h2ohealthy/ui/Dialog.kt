package org.n9ne.h2ohealthy.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.DialogAddBinding
import org.n9ne.h2ohealthy.databinding.DialogCupBinding
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener


fun Activity.addDialog(
    layoutInflater: LayoutInflater,
    selectedCup: Cup? = null,
    doneListener: CupClickListener? = null,
    selectListener: OnClickListener
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
            selectedCup?.let { cup ->
                click.onClick(cup)
                dialog.dismiss()
            }
        }
    }
    val blue = ResourcesCompat.getColor(this.resources, R.color.linearBlueEnd, this.theme)
    val white = ResourcesCompat.getColor(this.resources, R.color.white, this.theme)

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

fun Activity.cupDialog(
    layoutInflater: LayoutInflater
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogCupBinding.inflate(layoutInflater)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(binding.root)
    dialog.window?.setLayout(
        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)
    binding.ivBack.setOnClickListener {
        dialog.dismiss()
    }
    return dialog
}
