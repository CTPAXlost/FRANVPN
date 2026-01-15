package com.franvpn.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franvpn.app.data.model.VpnConfig
import com.franvpn.app.data.repository.ConfigRepository
import com.franvpn.app.vpn.PingChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for main connection state and server management
 */
class MainViewModel(private val configRepository: ConfigRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Idle)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    private val _sortBy = MutableStateFlow<SortOption>(SortOption.LastConnected)
    val sortBy: StateFlow<SortOption> = _sortBy.asStateFlow()
    
    val configs = configRepository.getAllConfigs()
    val favorites = configRepository.getFavoriteConfigs()
    
    fun updateSort(option: SortOption) {
        _sortBy.value = option
    }
    
    fun addConfigFromUri(uri: String) {
        viewModelScope.launch {
            _uiState.emit(MainUiState.Loading)
            configRepository.parseAndAddConfig(uri)
                .onSuccess {
                    _uiState.emit(MainUiState.Success("Config added successfully"))
                }
                .onFailure { error ->
                    _uiState.emit(MainUiState.Error(error.message ?: "Failed to add config"))
                }
        }
    }
    
    fun deleteConfig(config: VpnConfig) {
        viewModelScope.launch {
            configRepository.deleteConfig(config)
        }
    }
    
    fun toggleFavorite(configId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            configRepository.toggleFavorite(configId, isFavorite)
        }
    }
    
    fun measurePings(configs: List<VpnConfig>) {
        viewModelScope.launch {
            val pingChecker = PingChecker()
            configs.forEach { config ->
                try {
                    val ping = pingChecker.checkPing(config.server)
                    configRepository.updatePing(config.id, ping)
                } catch (e: Exception) {
                    Timber.e(e, "Ping check failed for ${config.server}")
                }
            }
        }
    }
    
    sealed class MainUiState {
        object Idle : MainUiState()
        object Loading : MainUiState()
        data class Success(val message: String) : MainUiState()
        data class Error(val message: String) : MainUiState()
    }
    
    enum class SortOption {
        LastConnected, Ping, Name, Protocol
    }
}

/**
 * ViewModel for subscription management
 */
class SubscriptionViewModel(private val repository: com.franvpn.app.data.repository.SubscriptionRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Idle)
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()
    
    val subscriptions = repository.getAllSubscriptions()
    
    fun addSubscription(url: String, name: String) {
        viewModelScope.launch {
            try {
                _uiState.emit(SubscriptionUiState.Loading)
                val subscription = com.franvpn.app.data.model.Subscription(
                    name = name,
                    url = url
                )
                repository.addSubscription(subscription)
                _uiState.emit(SubscriptionUiState.Success("Subscription added"))
            } catch (e: Exception) {
                _uiState.emit(SubscriptionUiState.Error(e.message ?: "Error"))
            }
        }
    }
    
    fun refreshSubscription(subscription: com.franvpn.app.data.model.Subscription) {
        viewModelScope.launch {
            _uiState.emit(SubscriptionUiState.Loading)
            repository.refreshSubscription(subscription)
                .onSuccess { nodeCount ->
                    _uiState.emit(SubscriptionUiState.Success("Updated: $nodeCount nodes"))
                }
                .onFailure { error ->
                    _uiState.emit(SubscriptionUiState.Error(error.message ?: "Refresh failed"))
                }
        }
    }
    
    fun deleteSubscription(subscription: com.franvpn.app.data.model.Subscription) {
        viewModelScope.launch {
            repository.deleteSubscription(subscription)
        }
    }
    
    sealed class SubscriptionUiState {
        object Idle : SubscriptionUiState()
        object Loading : SubscriptionUiState()
        data class Success(val message: String) : SubscriptionUiState()
        data class Error(val message: String) : SubscriptionUiState()
    }
}

/**
 * ViewModel for settings management
 */
class SettingsViewModel(private val repository: com.franvpn.app.data.repository.SettingsRepository) : ViewModel() {
    
    val settings = repository.getSettings()
    
    fun updateDns(dns: String) {
        viewModelScope.launch {
            repository.updateDns(dns)
        }
    }
    
    fun updateIpv6(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateIpv6(enabled)
        }
    }
    
    fun updateKillSwitch(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateKillSwitch(enabled)
        }
    }
    
    fun updateAutoReconnect(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAutoReconnect(enabled)
        }
    }
    
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            repository.updateTheme(theme)
        }
    }
}
