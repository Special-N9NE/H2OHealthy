package org.n9ne.common.dialog

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.mcdev.quantitizerlibrary.AnimationStyle
import org.n9ne.common.databinding.DialogReminderBinding
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Utils.setDialog
import org.n9ne.common.util.interfaces.ReminderSaveListener

fun Activity.reminderDialog(
    listener: ReminderSaveListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogReminderBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.setDialog()

    var value = Saver.getReminder()

    binding.apply {
        hq.maxValue = 24
        hq.minValue = 1
        hq.textAnimationStyle = AnimationStyle.SLIDE_IN_REVERSE
        hq.animationDuration = 150L
        hq.buttonAnimationEnabled = false
    }

    if (value == 0) {
        binding.cbReminder.isChecked = false
        binding.gHours.visibility = View.INVISIBLE
    } else {
        binding.cbReminder.isChecked = true
        binding.gHours.visibility = View.VISIBLE
        binding.hq.value = value
    }

    binding.cbReminder.setOnClickListener {
        binding.gHours.visibility =
            if (binding.cbReminder.isChecked) View.VISIBLE else View.INVISIBLE
    }
    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }
    binding.bSave.setOnClickListener {
        value = if (binding.cbReminder.isChecked)
            binding.hq.value
        else
            0

        listener.onSave(value)
        dialog.dismiss()
    }
    return dialog
}
