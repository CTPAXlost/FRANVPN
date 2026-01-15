package com.franvpn.app.data.parser

import android.util.Base64
import com.franvpn.app.data.model.VpnConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URI
import java.net.URLDecoder

/**
 * Protocol parser for VLESS, Trojan, Shadowsocks, and VMess URIs
 */
object ProtocolParser {

    /**
     * Parse any supported protocol URI
     * Supports: vless://, trojan://, ss://, vmess://
     */
    fun parseUri(uri: String): VpnConfig? {
        return try {
            when {
                uri.startsWith("vless://") -> parseVless(uri)
                uri.startsWith("trojan://") -> parseTrojan(uri)
                uri.startsWith("ss://") -> parseShadowsocks(uri)
                uri.startsWith("vmess://") -> parseVmess(uri)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse VLESS URI format:
     * vless://[user-id]@[server]:[port]?[parameters]#[remarks]
     */
    private fun parseVless(uri: String): VpnConfig? {
        try {
            val url = URI(uri)
            val userInfo = url.userInfo ?: return null
            val server = url.host ?: return null
            val port = url.port.takeIf { it > 0 } ?: 443
            val fragment = url.fragment ?: ""
            
            val params = parseQueryParams(url.query)
            
            return VpnConfig(
                protocol = "vless",
                name = URLDecoder.decode(fragment, "UTF-8").ifEmpty { "$server:$port" },
                server = server,
                port = port,
                uuid = userInfo,
                tlsEnabled = params["security"] == "tls" || params["security"] == "reality",
                tlsHost = params["sni"],
                transport = params["type"] ?: "tcp",
                transportHost = params["host"],
                transportPath = params["path"],
                remarks = fragment
            )
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Parse Trojan URI format:
     * trojan://[password]@[server]:[port]?[parameters]#[remarks]
     */
    private fun parseTrojan(uri: String): VpnConfig? {
        try {
            val url = URI(uri)
            val password = url.userInfo ?: return null
            val server = url.host ?: return null
            val port = url.port.takeIf { it > 0 } ?: 443
            val fragment = url.fragment ?: ""
            
            val params = parseQueryParams(url.query)
            
            return VpnConfig(
                protocol = "trojan",
                name = URLDecoder.decode(fragment, "UTF-8").ifEmpty { "$server:$port" },
                server = server,
                port = port,
                password = password,
                tlsEnabled = true,
                tlsHost = params["sni"] ?: server,
                transport = params["type"] ?: "tcp",
                transportPath = params["path"],
                remarks = fragment
            )
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Parse Shadowsocks URI format:
     * ss://[method]:[password]@[server]:[port]/?[parameters]#[remarks]
     * or
     * ss://[base64(method:password)]@[server]:[port]/#[remarks]
     */
    private fun parseShadowsocks(uri: String): VpnConfig? {
        try {
            val url = URI(uri)
            val userInfo = url.userInfo ?: return null
            val server = url.host ?: return null
            val port = url.port.takeIf { it > 0 } ?: 8388
            val fragment = url.fragment ?: ""
            
            val (method, password) = try {
                // Try to decode as base64 first
                val decoded = String(Base64.decode(userInfo, Base64.NO_PADDING or Base64.NO_WRAP))
                val parts = decoded.split(":", limit = 2)
                Pair(parts[0], parts.getOrNull(1) ?: "")
            } catch (e: Exception) {
                // Try plain format: method:password
                val parts = userInfo.split(":", limit = 2)
                Pair(parts[0], parts.getOrNull(1) ?: "")
            }
            
            return VpnConfig(
                protocol = "shadowsocks",
                name = URLDecoder.decode(fragment, "UTF-8").ifEmpty { "$server:$port" },
                server = server,
                port = port,
                password = password,
                encryptionMethod = method,
                remarks = fragment
            )
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Parse VMess URI format:
     * vmess://[base64(json_config)]
     * JSON should contain: {ps, add, port, id, aid, net, type, host, path, tls, sni}
     */
    private fun parseVmess(uri: String): VpnConfig? {
        try {
            val base64Part = uri.removePrefix("vmess://")
            val decoded = String(Base64.decode(base64Part, Base64.NO_PADDING or Base64.NO_WRAP))
            
            val json = Json.parseToJsonElement(decoded).jsonObject
            
            return VpnConfig(
                protocol = "vmess",
                name = json["ps"]?.jsonPrimitive?.content ?: "VMess Config",
                server = json["add"]?.jsonPrimitive?.content ?: return null,
                port = json["port"]?.jsonPrimitive?.content?.toIntOrNull() ?: 443,
                uuid = json["id"]?.jsonPrimitive?.content ?: return null,
                tlsEnabled = json["tls"]?.jsonPrimitive?.content == "tls",
                tlsHost = json["sni"]?.jsonPrimitive?.content,
                transport = json["net"]?.jsonPrimitive?.content ?: "tcp",
                transportHost = json["host"]?.jsonPrimitive?.content,
                transportPath = json["path"]?.jsonPrimitive?.content,
                remarks = json["ps"]?.jsonPrimitive?.content
            )
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Parse JSON config format (general V2Ray/Xray config)
     */
    fun parseJson(jsonString: String): VpnConfig? {
        return try {
            val json = Json.parseToJsonElement(jsonString).jsonObject
            
            // Try to detect protocol from outbound
            val outbound = json["outbound"]?.jsonObject ?: json["outbounds"]?.let {
                it.jsonObject["outbound"]?.jsonObject ?: it.jsonObject
            } ?: return null
            
            val protocol = outbound["protocol"]?.jsonPrimitive?.content ?: return null
            val settings = outbound["settings"]?.jsonObject ?: return null
            
            VpnConfig(
                protocol = protocol,
                name = json["name"]?.jsonPrimitive?.content ?: "Imported Config",
                server = settings["servers"]?.let { servers ->
                    servers.jsonObject["server"]?.jsonPrimitive?.content 
                } ?: settings["server"]?.jsonPrimitive?.content ?: "localhost",
                port = settings["port"]?.jsonPrimitive?.content?.toIntOrNull() ?: 443,
                remarks = json["remarks"]?.jsonPrimitive?.content
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Helper to parse query parameters
     */
    private fun parseQueryParams(query: String?): Map<String, String> {
        if (query == null) return emptyMap()
        
        return query.split("&")
            .mapNotNull { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) {
                    Pair(
                        URLDecoder.decode(parts[0], "UTF-8"),
                        URLDecoder.decode(parts[1], "UTF-8")
                    )
                } else null
            }
            .toMap()
    }

    /**
     * Convert URI list (from subscription) to configs
     * Each line should be a valid URI
     */
    fun parseSubscriptionContent(content: String): List<VpnConfig> {
        return content.split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { parseUri(it) }
    }
}
