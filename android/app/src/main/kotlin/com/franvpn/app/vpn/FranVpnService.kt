package com.franvpn.app.vpn

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.franvpn.app.R
import com.franvpn.app.data.model.VpnConfig
import com.franvpn.app.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Main VPN Service for FRANVPN
 * Implements Android VpnService API for creating VPN connections
 * Handles protocol-specific connection logic
 */
class FranVpnService : VpnService() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "franvpn_vpn"
        
        private var instance: FranVpnService? = null
        
        fun getInstance(): FranVpnService? = instance
        
        fun isRunning(): Boolean = instance != null && instance?.isServiceRunning == true
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private var vpnInterface: ParcelFileDescriptor? = null
    private var isServiceRunning = false
    
    private val _connectionState = MutableStateFlow<VpnConnectionState>(VpnConnectionState.Idle)
    val connectionState: StateFlow<VpnConnectionState> = _connectionState
    
    private var currentConfig: VpnConfig? = null
    private var xrayProcess: Process? = null
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        Timber.d("FranVpnService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        
        when (action) {
            ACTION_CONNECT -> {
                val config = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_CONFIG, VpnConfig::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_CONFIG) as? VpnConfig
                }
                
                if (config != null) {
                    connect(config)
                }
            }
            ACTION_DISCONNECT -> {
                disconnect()
            }
        }
        
        return START_STICKY
    }

    /**
     * Connect to a VPN server using the provided configuration
     */
    private fun connect(config: VpnConfig) {
        scope.launch {
            try {
                _connectionState.emit(VpnConnectionState.Connecting)
                currentConfig = config
                
                // Establish VPN interface
                vpnInterface = setupVpnInterface(config)
                
                // Start Xray process (simulated - actual implementation would use Xray core)
                startXrayProcess(config)
                
                isServiceRunning = true
                _connectionState.emit(VpnConnectionState.Connected(config))
                
                updateNotification(config, isConnected = true)
                Timber.d("Connected to ${config.name}")
                
            } catch (e: Exception) {
                Timber.e(e, "Connection failed: ${e.message}")
                _connectionState.emit(VpnConnectionState.Error(e.message ?: "Unknown error"))
                stopSelf()
            }
        }
    }

    /**
     * Setup VPN interface with proper configuration
     */
    private fun setupVpnInterface(config: VpnConfig): ParcelFileDescriptor {
        val builder = Builder()
            .setSession("FRANVPN - ${config.name}")
            .setMtu(1500)
        
        // Set DNS servers
        builder.addDnsServer("1.1.1.1")
        builder.addDnsServer("8.8.8.8")
        
        // Add IPv4 routes
        builder.addRoute("0.0.0.0", 0)
        
        // Add IPv6 routes (if enabled)
        try {
            builder.addRoute("::", 0)
        } catch (e: Exception) {
            Timber.w("IPv6 not supported: ${e.message}")
        }
        
        // Set app bypass (split tunneling) if configured
        try {
            // Example: bypass certain apps
            // builder.addDisallowedApplication("com.android.systemui")
        } catch (e: Exception) {
            Timber.w("Split tunneling setup failed: ${e.message}")
        }
        
        // Allow for VPN establishment
        builder.allowFamily(java.net.InetAddress.AF_INET)
        builder.allowFamily(java.net.InetAddress.AF_INET6)
        
        // Establish the VPN interface
        return builder.establish() ?: throw Exception("Failed to establish VPN interface")
    }

    /**
     * Start Xray process (placeholder for actual Xray core integration)
     * In production, this would:
     * 1. Extract Xray binary from assets
     * 2. Generate config file
     * 3. Start Xray process
     * 4. Redirect traffic through Xray
     */
    private fun startXrayProcess(config: VpnConfig) {
        // Placeholder: In real implementation, extract and run Xray binary
        Timber.d("Starting Xray process for ${config.protocol} on ${config.server}:${config.port}")
        
        // Real implementation would:
        // - Create Xray config JSON from VpnConfig
        // - Write to file
        // - Execute Xray binary with config
        // - Capture output and logs
    }

    /**
     * Disconnect from VPN
     */
    private fun disconnect() {
        scope.launch {
            try {
                isServiceRunning = false
                
                // Stop Xray process
                xrayProcess?.destroy()
                xrayProcess = null
                
                // Close VPN interface
                vpnInterface?.close()
                vpnInterface = null
                
                _connectionState.emit(VpnConnectionState.Disconnected)
                currentConfig = null
                
                updateNotification(null, isConnected = false)
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                
                Timber.d("VPN disconnected")
                
            } catch (e: Exception) {
                Timber.e(e, "Disconnect failed: ${e.message}")
            }
        }
    }

    /**
     * Update foreground notification
     */
    private fun updateNotification(config: VpnConfig?, isConnected: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val title = if (isConnected && config != null) {
            "Connected to ${config.name}"
        } else {
            "VPN Disconnected"
        }
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("Tap to open FRANVPN")
            .setSmallIcon(R.drawable.ic_vpn)
            .setContentIntent(pendingIntent)
            .setOngoing(isConnected)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }

    /**
     * Create notification channel for Android 8+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                "VPN Service",
                android.app.NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        scope.launch {
            disconnect()
        }
    }

    sealed class VpnConnectionState {
        object Idle : VpnConnectionState()
        object Connecting : VpnConnectionState()
        data class Connected(val config: VpnConfig) : VpnConnectionState()
        object Disconnected : VpnConnectionState()
        data class Error(val message: String) : VpnConnectionState()
    }

    companion object {
        const val ACTION_CONNECT = "com.franvpn.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.franvpn.vpn.DISCONNECT"
        const val EXTRA_CONFIG = "config"
    }
}
