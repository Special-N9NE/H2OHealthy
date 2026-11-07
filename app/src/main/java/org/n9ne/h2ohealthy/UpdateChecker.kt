package org.n9ne.h2ohealthy

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.n9ne.common.util.Utils.setDialog
import org.n9ne.h2ohealthy.databinding.DialogUpdateBinding
import java.io.IOException
import java.util.concurrent.TimeUnit

internal enum class UpdateKind { NONE, OPTIONAL, REQUIRED }

internal fun updateKind(installed: Long, latest: Long, minimum: Long): UpdateKind = when {
    installed < minimum -> UpdateKind.REQUIRED
    installed < latest -> UpdateKind.OPTIONAL
    else -> UpdateKind.NONE
}

/** One check per application process; dialogs follow whichever activity is foregrounded. */
internal class UpdateChecker(private val app: Application) : Application.ActivityLifecycleCallbacks {
    private val main = Handler(Looper.getMainLooper())
    private val prefs = app.getSharedPreferences("app_update", 0)
    private var activity: Activity? = null
    private var dialog: Dialog? = null
    private var latest = 0L
    private var kind = UpdateKind.NONE
    private var dismissedThisLaunch = false
    private var doNotShowAgain = false

    fun start() {
        app.registerActivityLifecycleCallbacks(this)
        val client = OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build()
        val request = Request.Builder()
            .url(org.n9ne.common.BuildConfig.API_BASE_URL + "app-version.php")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = Unit
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) return
                    val values = try {
                        val json = JSONObject(it.body?.string() ?: return)
                        if (json.opt("status") != true) return
                        val data = json.getJSONObject("data")
                        val latestValue = data.get("latest_version_code")
                        val minimumValue = data.get("min_supported_version_code")
                        if (latestValue !is Int || minimumValue !is Int ||
                            minimumValue <= 0 || latestValue < minimumValue) return
                        latestValue.toLong() to minimumValue.toLong()
                    } catch (_: Exception) { return }
                    main.post {
                        latest = values.first
                        val packageInfo = app.packageManager.getPackageInfo(app.packageName, 0)
                        kind = updateKind(PackageInfoCompat.getLongVersionCode(packageInfo), latest, values.second)
                        showIfNeeded()
                    }
                }
            }
        })
    }

    private fun showIfNeeded() {
        val host = activity ?: return
        if (host.isFinishing || host.isDestroyed || dialog != null || kind == UpdateKind.NONE) return
        if (kind == UpdateKind.OPTIONAL &&
            (dismissedThisLaunch || prefs.getLong("dismissed_version", 0L) == latest)) return

        val required = kind == UpdateKind.REQUIRED
        val binding = DialogUpdateBinding.inflate(host.layoutInflater)
        binding.updateTitle.setText(if (required) R.string.update_required_title else R.string.update_available_title)
        binding.updateMessage.setText(if (required) R.string.update_required_message else R.string.update_available_message)
        binding.updateDontShow.visibility = if (required) View.GONE else View.VISIBLE
        binding.updateLater.visibility = if (required) View.GONE else View.VISIBLE
        binding.updateDontShow.isChecked = doNotShowAgain
        binding.updateDontShow.setOnCheckedChangeListener { _, checked -> doNotShowAgain = checked }
        dialog = Dialog(host).also { alert ->
            alert.setContentView(binding.root)
            alert.setDialog()
            alert.setCancelable(false)
            binding.updateLater.setOnClickListener {
                dismissedThisLaunch = true
                if (binding.updateDontShow.isChecked) prefs.edit().putLong("dismissed_version", latest).apply()
                alert.dismiss()
            }
            alert.setOnDismissListener { if (dialog === alert) dialog = null }
            alert.show()
            alert.setDialog()
            binding.updateAction.setOnClickListener {
                try {
                    host.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://myket.ir/app/org.n9ne.h2ohealthy")))
                    if (!required) {
                        dismissedThisLaunch = true
                        if (binding.updateDontShow.isChecked) prefs.edit().putLong("dismissed_version", latest).apply()
                        alert.dismiss()
                    }
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(host, R.string.update_open_failed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResumed(activity: Activity) { this.activity = activity; showIfNeeded() }
    override fun onActivityPaused(activity: Activity) {
        if (this.activity === activity) {
            this.activity = null
            dialog?.dismiss()
            dialog = null
        }
    }
    override fun onActivityCreated(activity: Activity, state: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, state: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
