package org.n9ne.h2ohealthy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.DialogAddBinding
import org.n9ne.h2ohealthy.databinding.DialogAddCupBinding
import org.n9ne.h2ohealthy.databinding.DialogCupBinding
import org.n9ne.h2ohealthy.util.interfaces.AddWaterListener
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener
import kotlin.math.roundToInt

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

fun Activity.addCupDialog(
    cup: Cup?,
    listener: CupClickListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogAddCupBinding.inflate(layoutInflater)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(binding.root)
    dialog.window?.setLayout(
        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)

    cup?.let {
        binding.etName.setText(it.title)
        binding.etAmount.setText(it.capacity.toString())
        binding.cvColor.strokeColor = Color.parseColor(it.color)
        binding.etColor.setText("Color is selected")
    }

    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }
    binding.cvColor.setOnClickListener {
        //TODO open color picker
    }
    binding.bDone.setOnClickListener {
        //TODO validation
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString()
        val color = "#92A3FD"

        //TODO change id user
        val result = Cup(cup?.id,1L , name, amount.toDouble().roundToInt(), color)
        listener.onClick(result)
        dialog.dismiss()
    }

    return dialog
}