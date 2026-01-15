package com.franvpn.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.franvpn.app.data.model.AppSettings
import com.franvpn.app.data.model.Subscription
import com.franvpn.app.data.model.VpnConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    
    @Query("SELECT * FROM configs ORDER BY lastConnected DESC, name ASC")
    fun getAllConfigs(): Flow<List<VpnConfig>>
    
    @Query("SELECT * FROM configs WHERE id = :id")
    suspend fun getConfigById(id: Long): VpnConfig?
    
    @Query("SELECT * FROM configs WHERE groupId = :groupId ORDER BY name ASC")
    fun getConfigsByGroup(groupId: Long): Flow<List<VpnConfig>>
    
    @Query("SELECT * FROM configs WHERE favorite = 1 ORDER BY lastConnected DESC")
    fun getFavoriteConfigs(): Flow<List<VpnConfig>>
    
    @Insert
    suspend fun insertConfig(config: VpnConfig): Long
    
    @Update
    suspend fun updateConfig(config: VpnConfig)
    
    @Delete
    suspend fun deleteConfig(config: VpnConfig)
    
    @Query("DELETE FROM configs WHERE groupId = :groupId")
    suspend fun deleteConfigsByGroup(groupId: Long)
    
    @Query("UPDATE configs SET ping = :ping WHERE id = :id")
    suspend fun updatePing(id: Long, ping: Int)
    
    @Query("UPDATE configs SET lastConnected = :timestamp WHERE id = :id")
    suspend fun updateLastConnected(id: Long, timestamp: Long)
    
    @Query("UPDATE configs SET favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM configs")
    suspend fun getConfigCount(): Int
}

@Dao
interface SubscriptionDao {
    
    @Query("SELECT * FROM subscriptions ORDER BY name ASC")
    fun getAllSubscriptions(): Flow<List<Subscription>>
    
    @Query("SELECT * FROM subscriptions WHERE enabled = 1")
    fun getEnabledSubscriptions(): Flow<List<Subscription>>
    
    @Query("SELECT * FROM subscriptions WHERE id = :id")
    suspend fun getSubscriptionById(id: Long): Subscription?
    
    @Insert
    suspend fun insertSubscription(subscription: Subscription): Long
    
    @Update
    suspend fun updateSubscription(subscription: Subscription)
    
    @Delete
    suspend fun deleteSubscription(subscription: Subscription)
    
    @Query("UPDATE subscriptions SET lastUpdated = :timestamp WHERE id = :id")
    suspend fun updateLastUpdated(id: Long, timestamp: Long)
}

@Dao
interface SettingsDao {
    
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<AppSettings?>
    
    @Insert
    suspend fun insertSettings(settings: AppSettings)
    
    @Update
    suspend fun updateSettings(settings: AppSettings)
    
    @Query("UPDATE settings SET dns = :dns WHERE id = 1")
    suspend fun updateDns(dns: String)
    
    @Query("UPDATE settings SET ipv6Enabled = :enabled WHERE id = 1")
    suspend fun updateIpv6(enabled: Boolean)
    
    @Query("UPDATE settings SET killSwitchEnabled = :enabled WHERE id = 1")
    suspend fun updateKillSwitch(enabled: Boolean)
    
    @Query("UPDATE settings SET autoReconnect = :enabled WHERE id = 1")
    suspend fun updateAutoReconnect(enabled: Boolean)
    
    @Query("UPDATE settings SET theme = :theme WHERE id = 1")
    suspend fun updateTheme(theme: String)
}
