package org.n9ne.common.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.text.isDigitsOnly
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import org.n9ne.common.R
import org.n9ne.common.databinding.DialogAddCupBinding
import org.n9ne.common.databinding.DialogCupBinding
import org.n9ne.common.model.Cup
import org.n9ne.common.util.Utils
import org.n9ne.common.util.Utils.setDialog
import org.n9ne.common.util.interfaces.CupClickListener
import kotlin.math.roundToInt

fun Activity.cupDialog(
    layoutInflater: LayoutInflater
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogCupBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.setDialog()

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
    dialog.setContentView(binding.root)

    dialog.setDialog()

    var color = "#92A3FD"
    cup?.let {
        color = it.color
        binding.etName.setText(it.title)
        binding.etAmount.setText(it.capacity.toString())
        binding.cvColor.strokeColor = Color.parseColor(it.color)
        binding.etColor.setText(getString(R.string.colorSelected))
    }

    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }
    binding.etColor.setOnClickListener {
        val title = if (Utils.isLocalPersian()) "انتخاب رنگ" else "Pick Color"
        MaterialColorPickerDialog.Builder(this)
            .setTitle(title)
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


        val result = Cup(cup?.id, null, name, amount.toDouble().roundToInt(), color)
        listener.onClick(result)
        dialog.dismiss()
    }
    return dialog
}