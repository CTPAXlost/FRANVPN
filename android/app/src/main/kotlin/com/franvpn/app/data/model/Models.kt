package com.franvpn.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a VPN configuration from any protocol
 * Supports: VLESS, Trojan, Shadowsocks, VMess
 */
@Entity(tableName = "configs")
@Serializable
data class VpnConfig(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val protocol: String, // vless, trojan, shadowsocks, vmess
    val server: String,
    val port: Int,
    val password: String? = null,
    val username: String? = null,
    val uuid: String? = null,
    
    // Protocol-specific parameters
    @SerialName("tls")
    val tlsEnabled: Boolean = false,
    @SerialName("tls_host")
    val tlsHost: String? = null,
    @SerialName("transport")
    val transport: String = "tcp", // tcp, ws, grpc, kcp, quic
    @SerialName("transport_host")
    val transportHost: String? = null,
    @SerialName("transport_path")
    val transportPath: String? = null,
    
    // Shadowsocks specific
    @SerialName("method")
    val encryptionMethod: String? = null, // aes-256-gcm, chacha20-poly1305, etc
    
    // Custom fields
    val remarks: String? = null,
    val groupId: Long? = null,
    val ping: Int = -1,
    val lastConnected: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val favorite: Boolean = false
)

/**
 * Subscription model for managing multiple configs
 */
@Entity(tableName = "subscriptions")
@Serializable
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val url: String,
    val lastUpdated: Long = 0L,
    val updateInterval: Long = 3600000, // 1 hour default
    val enabled: Boolean = true,
    val nodeCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Connection state model
 */
data class ConnectionState(
    val isConnected: Boolean = false,
    val currentConfig: VpnConfig? = null,
    val uploadSpeed: Long = 0L, // bytes/s
    val downloadSpeed: Long = 0L, // bytes/s
    val uploadTotal: Long = 0L, // total bytes
    val downloadTotal: Long = 0L, // total bytes
    val connectionTime: Long = 0L, // timestamp
    val durationSeconds: Long = 0L,
    val errorMessage: String? = null
)

/**
 * App settings model
 */
@Entity(tableName = "settings")
data class AppSettings(
    @PrimaryKey
    val id: Int = 1,
    
    val theme: String = "dark", // dark, light, system
    val language: String = "en",
    val dns: String = "1.1.1.1", // Cloudflare DNS
    val dnsv6: String = "2606:4700:4700::1111",
    val ipv6Enabled: Boolean = true,
    val killSwitchEnabled: Boolean = true,
    val autoReconnect: Boolean = true,
    val splitTunnelingEnabled: Boolean = false,
    val allowBypass: Boolean = false,
    val showNotification: Boolean = true,
    val autoStartOnBoot: Boolean = false,
    val routingRules: String = "" // JSON serialized
)
