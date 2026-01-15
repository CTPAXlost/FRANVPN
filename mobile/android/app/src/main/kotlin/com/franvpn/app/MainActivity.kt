package com.franvpn.app

import android.content.Context
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        
        // Setup VPN service channel
        val channel = "com.franvpn/vpn"
        val methodChannel = io.flutter.embedding.engine.deferredcomponents.PlayStoreDynamicFeatureManager.MethodChannelMessenger(
            flutterEngine.dartExecutor.binaryMessenger,
            channel
        )
    }
}

/**
 * Simple VPN Service implementation for FRANVPN
 * This handles the actual VPN connection at the system level
 */
class FranVPNService : VpnService() {
    
    private var vpnInterface: ParcelFileDescriptor? = null
    
    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        // Setup VPN
        setupVPN()
        return START_STICKY
    }
    
    private fun setupVPN() {
        // Create VPN builder
        val builder = Builder()
        
        // Set session name
        builder.setSession("FRANVPN")
        
        // Configure routes and DNS
        builder.addAddress("10.0.0.2", 32)
        builder.addRoute("0.0.0.0", 0)
        builder.addDnsServer("1.1.1.1") // Cloudflare DNS
        
        // Establish VPN
        vpnInterface = builder.establish()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        vpnInterface?.close()
    }
}
