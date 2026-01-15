package com.franvpn.app.data.repository

import com.franvpn.app.data.api.SubscriptionApi
import com.franvpn.app.data.db.ConfigDao
import com.franvpn.app.data.db.SettingsDao
import com.franvpn.app.data.db.SubscriptionDao
import com.franvpn.app.data.model.AppSettings
import com.franvpn.app.data.model.Subscription
import com.franvpn.app.data.model.VpnConfig
import com.franvpn.app.data.parser.ProtocolParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for VPN configuration management
 */
class ConfigRepository(
    private val configDao: ConfigDao,
    private val subscriptionApi: SubscriptionApi
) {
    
    fun getAllConfigs(): Flow<List<VpnConfig>> = configDao.getAllConfigs()
    
    fun getFavoriteConfigs(): Flow<List<VpnConfig>> = configDao.getFavoriteConfigs()
    
    fun getConfigsByGroup(groupId: Long): Flow<List<VpnConfig>> = configDao.getConfigsByGroup(groupId)
    
    suspend fun addConfig(config: VpnConfig): Long = withContext(Dispatchers.IO) {
        configDao.insertConfig(config)
    }
    
    suspend fun updateConfig(config: VpnConfig) = withContext(Dispatchers.IO) {
        configDao.updateConfig(config)
    }
    
    suspend fun deleteConfig(config: VpnConfig) = withContext(Dispatchers.IO) {
        configDao.deleteConfig(config)
    }
    
    suspend fun updatePing(configId: Long, ping: Int) = withContext(Dispatchers.IO) {
        configDao.updatePing(configId, ping)
    }
    
    suspend fun updateLastConnected(configId: Long) = withContext(Dispatchers.IO) {
        configDao.updateLastConnected(configId, System.currentTimeMillis())
    }
    
    suspend fun toggleFavorite(configId: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        configDao.updateFavorite(configId, isFavorite)
    }
    
    suspend fun parseAndAddConfig(uri: String): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val config = ProtocolParser.parseUri(uri) ?: return@withContext Result.failure(
                Exception("Unsupported protocol or invalid URI")
            )
            val id = configDao.insertConfig(config)
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse config URI")
            Result.failure(e)
        }
    }
    
    suspend fun importJsonConfig(jsonString: String): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val config = ProtocolParser.parseJson(jsonString) ?: return@withContext Result.failure(
                Exception("Invalid JSON config")
            )
            val id = configDao.insertConfig(config)
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse JSON config")
            Result.failure(e)
        }
    }
}

/**
 * Repository for subscription management
 */
class SubscriptionRepository(
    private val subscriptionDao: SubscriptionDao,
    private val configDao: ConfigDao,
    private val subscriptionApi: SubscriptionApi
) {
    
    fun getAllSubscriptions(): Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()
    
    fun getEnabledSubscriptions(): Flow<List<Subscription>> = subscriptionDao.getEnabledSubscriptions()
    
    suspend fun addSubscription(subscription: Subscription): Long = withContext(Dispatchers.IO) {
        subscriptionDao.insertSubscription(subscription)
    }
    
    suspend fun updateSubscription(subscription: Subscription) = withContext(Dispatchers.IO) {
        subscriptionDao.updateSubscription(subscription)
    }
    
    suspend fun deleteSubscription(subscription: Subscription) = withContext(Dispatchers.IO) {
        configDao.deleteConfigsByGroup(subscription.id)
        subscriptionDao.deleteSubscription(subscription)
    }
    
    suspend fun refreshSubscription(subscription: Subscription): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val response = subscriptionApi.getSubscription(subscription.url)
            val content = response.string()
            
            // Parse configs from subscription content
            val configs = ProtocolParser.parseSubscriptionContent(content)
            
            // Delete old configs from this subscription
            configDao.deleteConfigsByGroup(subscription.id)
            
            // Add new configs
            configs.forEach { config ->
                configDao.insertConfig(config.copy(groupId = subscription.id))
            }
            
            // Update subscription metadata
            subscriptionDao.updateSubscription(
                subscription.copy(
                    lastUpdated = System.currentTimeMillis(),
                    nodeCount = configs.size
                )
            )
            
            Timber.d("Subscription refreshed: ${configs.size} nodes")
            Result.success(configs.size)
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh subscription")
            Result.failure(e)
        }
    }
}

/**
 * Repository for app settings
 */
class SettingsRepository(private val settingsDao: SettingsDao) {
    
    fun getSettings(): Flow<AppSettings?> = settingsDao.getSettings()
    
    suspend fun initializeSettings() = withContext(Dispatchers.IO) {
        val existing = settingsDao.getSettings()
        // Settings initialization handled by flow emission
    }
    
    suspend fun updateDns(dns: String) = withContext(Dispatchers.IO) {
        settingsDao.updateDns(dns)
    }
    
    suspend fun updateIpv6(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingsDao.updateIpv6(enabled)
    }
    
    suspend fun updateKillSwitch(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingsDao.updateKillSwitch(enabled)
    }
    
    suspend fun updateAutoReconnect(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingsDao.updateAutoReconnect(enabled)
    }
    
    suspend fun updateTheme(theme: String) = withContext(Dispatchers.IO) {
        settingsDao.updateTheme(theme)
    }
}
