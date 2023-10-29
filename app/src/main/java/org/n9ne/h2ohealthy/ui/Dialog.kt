package org.n9ne.h2ohealthy.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.FrameLayout
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.DialogAddBinding
import org.n9ne.h2ohealthy.databinding.DialogCupBinding
import org.n9ne.h2ohealthy.ui.home.adpter.CupsAdapter


fun Activity.addDialog(layoutInflater: LayoutInflater, listener: OnClickListener): Dialog {
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
        listener.onClick(binding.cvCup)
    }

    return dialog
}

fun Activity.cupDialog(layoutInflater: LayoutInflater, cups: List<Cup>): Dialog {
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

    binding.rvCup.adapter = CupsAdapter(cups)


    return dialog
}
