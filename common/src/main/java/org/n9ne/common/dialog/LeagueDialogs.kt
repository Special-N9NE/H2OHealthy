package org.n9ne.common.dialog

import android.app.Activity
import android.app.Dialog
import android.view.View
import org.n9ne.common.R.string
import org.n9ne.common.databinding.DialogCreateLeagueBinding
import org.n9ne.common.databinding.DialogJoinLeagueBinding
import org.n9ne.common.databinding.DialogLeagueSettingBinding
import org.n9ne.common.util.Utils.setDialog
import org.n9ne.common.util.interfaces.AddLeagueListener


fun Activity.createLeagueDialog(
    editMode: Boolean,
    joinListener: View.OnClickListener?,
    createListener: AddLeagueListener
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogCreateLeagueBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.setDialog()

    binding.ivClose.setOnClickListener {
        dialog.dismiss()
    }

    if (editMode) {
        binding.tvJoin.visibility = View.GONE
        binding.tvTitle.text = getString(string.rename_league)
        binding.bCreate.text = getString(string.rename)
    } else {
        binding.tvJoin.visibility = View.VISIBLE
        binding.tvTitle.text = getString(string.create_league)
        binding.bCreate.text = getString(string.create)
    }

    binding.tvJoin.setOnClickListener {
        joinListener!!.onClick(binding.tvJoin)
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
    dialog.setContentView(binding.root)

    dialog.setDialog()

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
    dialog.setContentView(binding.root)

    dialog.setDialog()

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