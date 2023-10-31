package org.n9ne.h2ohealthy.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.DialogAddBinding
import org.n9ne.h2ohealthy.databinding.DialogCreateLeagueBinding
import org.n9ne.h2ohealthy.databinding.DialogCupBinding
import org.n9ne.h2ohealthy.databinding.DialogJoinLeagueBinding
import org.n9ne.h2ohealthy.databinding.DialogLeagueSettingBinding
import org.n9ne.h2ohealthy.util.interfaces.AddLeagueListener
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

fun Activity.createLeagueDialog(
    joinListener: OnClickListener,
    createListener: AddLeagueListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogCreateLeagueBinding.inflate(layoutInflater)
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
    binding.tvJoin.setOnClickListener {
        joinListener.onClick(binding.tvJoin)
    }
    binding.bCreate.setOnClickListener {
        createListener.addLeague(binding.etName.text.toString())
    }
    return dialog
}

fun Activity.joinLeagueDialog(
    listener: AddLeagueListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogJoinLeagueBinding.inflate(layoutInflater)
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
    binding.bJoin.setOnClickListener {
        listener.addLeague(binding.etCode.text.toString())
    }
    return dialog
}

fun Activity.leagueSettingDialog(
    name: String,
    code: String,
    renameListener: OnClickListener?,
    shareListener: OnClickListener,
    removeListener: OnClickListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogLeagueSettingBinding.inflate(layoutInflater)
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

    binding.tvName.text = name
    binding.tvCode.text = "@${code}"


    binding.cvRename.visibility = if (renameListener == null) View.GONE else View.VISIBLE

    binding.cvRename.setOnClickListener {
        renameListener?.let {
            it.onClick(binding.cvRename)
            dialog.dismiss()
        }
    }
    binding.cvShare.setOnClickListener {
        shareListener.onClick(binding.cvShare)
        dialog.dismiss()
    }
    binding.cvLeave.setOnClickListener {
        removeListener.onClick(binding.cvLeave)
        dialog.dismiss()
    }
    return dialog
}