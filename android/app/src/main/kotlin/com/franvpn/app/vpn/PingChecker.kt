package com.franvpn.app.vpn

import java.io.IOException
import java.net.InetAddress

/**
 * Utility for measuring ping/latency to servers
 */
class PingChecker {
    
    /**
     * Check ping to a host using ICMP
     * Returns latency in milliseconds
     */
    suspend fun checkPing(host: String, count: Int = 3): Int {
        return try {
            var totalTime = 0L
            var successCount = 0
            
            repeat(count) {
                try {
                    val startTime = System.currentTimeMillis()
                    val address = InetAddress.getByName(host)
                    val isReachable = address.isReachable(5000) // 5 second timeout
                    val endTime = System.currentTimeMillis()
                    
                    if (isReachable) {
                        val time = (endTime - startTime).toInt()
                        totalTime += time
                        successCount++
                    }
                } catch (e: IOException) {
                    // Skip this attempt
                }
            }
            
            if (successCount > 0) {
                (totalTime / successCount).toInt()
            } else {
                -1 // Unreachable
            }
        } catch (e: Exception) {
            -1
        }
    }
    
    /**
     * Get ping indicator color
     * Green: < 80ms
     * Yellow: 80-150ms
     * Red: > 150ms
     */
    fun getPingColor(ping: Int): PingColor {
        return when {
            ping < 0 -> PingColor.Unknown
            ping < 80 -> PingColor.Good
            ping < 150 -> PingColor.Normal
            else -> PingColor.Bad
        }
    }
    
    enum class PingColor {
        Good,      // Green
        Normal,    // Yellow
        Bad,       // Red
        Unknown    // Gray
    }
}
