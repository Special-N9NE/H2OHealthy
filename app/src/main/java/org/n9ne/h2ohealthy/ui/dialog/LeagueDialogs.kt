package org.n9ne.h2ohealthy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.FrameLayout
import org.n9ne.h2ohealthy.databinding.DialogCreateLeagueBinding
import org.n9ne.h2ohealthy.databinding.DialogJoinLeagueBinding
import org.n9ne.h2ohealthy.databinding.DialogLeagueSettingBinding
import org.n9ne.h2ohealthy.util.interfaces.AddLeagueListener


fun Activity.createLeagueDialog(
    joinListener: View.OnClickListener,
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
    renameListener: View.OnClickListener?,
    shareListener: View.OnClickListener,
    removeListener: View.OnClickListener
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