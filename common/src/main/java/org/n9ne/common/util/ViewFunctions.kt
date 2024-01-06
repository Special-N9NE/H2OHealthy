package org.n9ne.common.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import org.n9ne.common.R

fun FragmentActivity.requestNotificationPermission() {
    val permissions = listOf(Manifest.permission.POST_NOTIFICATIONS)

    val text = getString(R.string.permissionText)
    val ok = getString(R.string.ok)
    val cancel = getString(R.string.cancel)
    PermissionX.init(this)
        .permissions(permissions)
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                text,
                ok,
                cancel
            )
        }
        .request { allGranted, grantedList, deniedList ->
        }

}

fun Context.reminderNotification(enable: Boolean) {
    if (enable) {
        val name = "Reminder"
        val text = "Reminder for drinking water"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("1", name, importance).apply {
            description = text
        }
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    } else {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = "1"
        notificationManager.deleteNotificationChannel(id)
    }
}

fun Context.scheduleNotification(hours: Int) {

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis(),
        hours * 60 * 60 * 1000L,
        pendingIntent
    )
}

fun Context.isDeviceTablet(): Boolean {
    val resources = resources
    val configuration = resources.configuration
    val screenWidthDp = configuration.screenWidthDp

    return screenWidthDp > 600
}


fun Context.dpToPx(dp: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics
).toInt()

fun ImageView.setUserAvatar(image: String) {
    if (image.isNotEmpty()) {
        val resId = resources.getIdentifier(
            image, "drawable", this.context.packageName
        )
        if (resId != 0) {
            this.setImageResource(resId)
        }
    }
}

fun TextView.setGradient(context: Context, @ColorRes startColor: Int, @ColorRes endColor: Int) {
    val textShader: Shader = LinearGradient(
        0f,
        0f,
        100f,
        0f,
        intArrayOf(
            context.getColor(startColor),
            context.getColor(endColor)
        ),
        floatArrayOf(0f, 1f),
        Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}