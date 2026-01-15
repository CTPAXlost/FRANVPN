package com.franvpn.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.franvpn.app.data.model.AppSettings
import com.franvpn.app.data.model.Subscription
import com.franvpn.app.data.model.VpnConfig

@Database(
    entities = [VpnConfig::class, Subscription::class, AppSettings::class],
    version = 1,
    exportSchema = false
)
abstract class FranVpnDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun settingsDao(): SettingsDao
}
