package com.franvpn.app.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

/**
 * Broadcast receiver for device boot
 * Optionally starts VPN if auto-start is enabled
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Timber.d("Device booted, checking auto-start setting")
            // TODO: Check settings and potentially restart VPN
        }
    }
}
