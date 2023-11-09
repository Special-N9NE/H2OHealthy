package org.n9ne.h2ohealthy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.text.isDigitsOnly
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.DialogAddCupBinding
import org.n9ne.h2ohealthy.databinding.DialogCupBinding
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
    var color = "#92A3FD"
    binding.etColor.setOnClickListener {
        MaterialColorPickerDialog.Builder(this)
            .setTitle("Pick Color")
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(color)
            .setColorListener { _, colorHex ->
                color = colorHex
                binding.cvColor.strokeColor = Color.parseColor(color)
            }.show()
    }
    binding.bDone.setOnClickListener {
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString()

        if (name.isEmpty()) {
            binding.cvName.strokeColor = Color.RED

            return@setOnClickListener
        } else
            binding.cvName.strokeColor = Color.TRANSPARENT

        if (amount.isEmpty() || !amount.isDigitsOnly() || amount.toDouble() <= 0.0) {
            binding.cvAmount.strokeColor = Color.RED

            return@setOnClickListener
        } else
            binding.cvAmount.strokeColor = Color.TRANSPARENT


        //TODO change id user
        val result = Cup(cup?.id, 1L, name, amount.toDouble().roundToInt(), color)
        listener.onClick(result)
        dialog.dismiss()
    }
    return dialog
}