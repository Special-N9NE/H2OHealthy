package org.n9ne.h2ohealthy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseActivity
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Utils
import org.n9ne.h2ohealthy.data.MainRepo
import org.n9ne.h2ohealthy.data.MainRepoApiImpl
import org.n9ne.h2ohealthy.databinding.ActivityForgetBinding

@AndroidEntryPoint
class ForgetActivity : BaseActivity() {

    private lateinit var viewModel: ForgetViewModel
    private lateinit var b: ActivityForgetBinding
    private lateinit var apiRepo: MainRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityForgetBinding.inflate(layoutInflater)
        setLocal()
        setContentView(b.root)

        init()
        observers()

        val data = intent.data

        if (data == null) {

            val text = if (Utils.isLocalPersian()) "اطلاعات نامعتبر" else "invalid data"

            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://home")
            )
            startActivity(intent)
            finish()
        } else {
            click()
        }
    }

    private fun click() {
        b.bDone.setOnClickListener {
            b.bDone.isEnabled = false
            val email = viewModel.decodeEmail(intent.data!!.pathSegments[0])
            val password = b.etPassword.text.toString().trim()
            val encrypted = Utils.encrypt(password)
            viewModel.resetPassword(encrypted, email)
        }
    }

    private fun init() {
        apiRepo = MainRepoApiImpl(Client.getInstance())
        viewModel = ViewModelProvider(this)[ForgetViewModel::class.java]
        viewModel.repo = apiRepo
    }

    private fun observers() {
        viewModel.ldRest.observe(this, EventObserver(listOf(b.bDone)) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://auth")
            )
            startActivity(intent)
            finish()
        })
        viewModel.ldError.observe(this, EventObserver(listOf(b.bDone)) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}