# FRANVPN – Technical Architecture

---

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                       │
│                      FRANVPN CLIENT APPLICATION                      │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                   PRESENTATION LAYER (UI/UX)               │    │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │    │
│  │  │   Desktop    │  │   Web App    │  │   Mobile     │     │    │
│  │  │ (Electron)   │  │  (React PWA) │  │  (Flutter)   │     │    │
│  │  └──────────────┘  └──────────────┘  └──────────────┘     │    │
│  └────────────────────────┬─────────────────────────────────┘    │
│                           │                                        │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                   APPLICATION LOGIC LAYER                   │    │
│  │  ┌──────────────────────────────────────────────────────┐   │    │
│  │  │  Configuration Manager │ Profile Manager │ Stats Mgr│   │    │
│  │  │  Server Manager │ DNS Manager │ Notifications        │   │    │
│  │  └──────────────────────────────────────────────────────┘   │    │
│  │  ┌──────────────────────────────────────────────────────┐   │    │
│  │  │  Protocol Engine (Abstraction Layer)                 │   │    │
│  │  │  VLESS │ VMess │ Trojan │ SS │ SOCKS5              │   │    │
│  │  │  TLS │ REALITY │ XTLS │ WebSocket │ gRPC │ QUIC   │   │    │
│  │  └──────────────────────────────────────────────────────┘   │    │
│  │  ┌──────────────────────────────────────────────────────┐   │    │
│  │  │  Security Layer                                      │   │    │
│  │  │  Encryption │ Memory Protection │ Secure Logging    │   │    │
│  │  └──────────────────────────────────────────────────────┘   │    │
│  └────────────────────────┬─────────────────────────────────┘    │
│                           │                                        │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                   PLATFORM-SPECIFIC LAYER                   │    │
│  │  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌──────────┐ │    │
│  │  │  Windows   │ │  macOS     │ │  Linux     │ │ iOS/Droid│ │    │
│  │  │  (WinTun)  │ │  (NE Ext)  │ │  (netlink) │ │ (VPN API)│ │    │
│  │  └────────────┘ └────────────┘ └────────────┘ └──────────┘ │    │
│  └────────────────────────┬─────────────────────────────────┘    │
│                           │                                        │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                      V2RAY CORE (Embedded)                   │    │
│  │  Protocol Implementation │ Routing │ Transport Handling     │    │
│  │  DNS Handling │ Proxy Chains │ Load Balancing              │    │
│  └────────────────────────┬─────────────────────────────────┘    │
│                           │                                        │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    OPERATING SYSTEM APIs                    │    │
│  │  Network Stack │ Firewall │ Kernel VPN │ System Services   │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Core Technology Stack

### Cross-Platform Foundation

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **VPN Engine** | V2Ray (embedded) | Protocol handling, routing, tunneling |
| **Build Tool** | Electron / Tauri | Desktop cross-platform |
| **UI Framework** | React / Vue | Desktop & web UI |
| **Mobile** | Flutter | iOS & Android native |
| **Package Manager** | npm / CocoaPods | Dependency management |
| **Database** | SQLite (local) | Config, profiles, logs storage |

### Desktop Application (Electron / Tauri)

**Electron Approach:**
- **Pros:** Rich ecosystem, JavaScript-based, mature
- **Cons:** Larger binary size (~150MB), higher memory usage
- **Best For:** Rapid MVP, cross-platform features

**Tauri Approach:**
- **Pros:** Smaller binary (~15MB), minimal memory, native performance
- **Cons:** Newer, smaller community
- **Best For:** Long-term, resource-efficient deployment

**Recommendation:** Start with **Tauri** for long-term vision

### Desktop Stack (Tauri)

```
┌──────────────────────────────────┐
│  Tauri (Desktop Framework)       │
│  ├─ Rust backend (core logic)    │
│  ├─ WebView frontend (UI)        │
│  └─ IPC bridge (communication)   │
├──────────────────────────────────┤
│  Frontend:                       │
│  ├─ React / Vue (UI framework)   │
│  ├─ TailwindCSS (styling)        │
│  ├─ TypeScript (type safety)     │
│  └─ Redux / Pinia (state mgmt)   │
├──────────────────────────────────┤
│  Backend (Rust):                 │
│  ├─ tokio (async runtime)        │
│  ├─ serde (serialization)        │
│  ├─ rusqlite (SQLite bindings)   │
│  └─ subprocess (process mgmt)    │
└──────────────────────────────────┘
```

### Mobile Stack (Flutter)

```
┌──────────────────────────────────┐
│  Flutter (Mobile Framework)      │
│  ├─ Dart language                │
│  ├─ Native iOS/Android bindings  │
│  └─ Hot reload development       │
├──────────────────────────────────┤
│  UI Layer:                       │
│  ├─ Material Design (Android)    │
│  ├─ Cupertino (iOS)              │
│  ├─ Custom widgets               │
│  └─ Animation framework          │
├──────────────────────────────────┤
│  Platform Integration:           │
│  ├─ VpnService API (Android)     │
│  ├─ NEVPNManager (iOS)           │
│  ├─ SharedPreferences (storage)  │
│  └─ Native channel communication │
└──────────────────────────────────┘
```

---

## Module Architecture

### 1. Configuration Module

**Responsibility:** Load, save, validate configurations

**Components:**
- `ConfigLoader` – Load from file/QR/URL
- `ConfigValidator` – Validate protocol syntax
- `ConfigBuilder` – Create valid V2Ray configs
- `ConfigEncryption` – Encrypt sensitive data locally

**Data Structure:**
```json
{
  "version": "1.0",
  "profile": {
    "name": "Work VPN",
    "server": {
      "address": "203.0.113.100",
      "port": 443
    },
    "protocol": {
      "type": "vless",
      "settings": {
        "clients": [{"id": "uuid"}]
      }
    },
    "transport": {
      "type": "grpc",
      "grpcSettings": {}
    },
    "security": {
      "type": "tls",
      "tlsSettings": {"serverName": "example.com"}
    }
  }
}
```

**External APIs:**
```
- loadConfig(path: string)
- saveConfig(profile: Profile)
- validateConfig(config: V2RayConfig)
- importFromQR(qrData: string)
- importFromURL(subscriptionURL: string)
```

---

### 2. Protocol Engine Module

**Responsibility:** Abstraction layer above V2Ray

**Protocols Supported:**
- VLESS (recommended)
- VMess
- Trojan
- Shadowsocks
- SOCKS5
- HTTP/HTTPS proxy

**Transport Layers:**
- WebSocket
- gRPC
- QUIC
- Raw TCP

**Security Features:**
- TLS 1.3
- XTLS (v2ray-specific)
- REALITY (anti-detection)

**Interface:**
```typescript
interface ProtocolHandler {
  type: 'vless' | 'vmess' | 'trojan' | 'ss' | 'socks5';
  connect(config: ProtocolConfig): Promise<Connection>;
  disconnect(): Promise<void>;
  getMetrics(): ConnectionMetrics;
  validateConfig(config: any): boolean;
}

interface TransportHandler {
  type: 'ws' | 'grpc' | 'quic' | 'tcp';
  setup(settings: TransportSettings): void;
  send(data: Buffer): Promise<void>;
  receive(): Promise<Buffer>;
}
```

**Implementation:**
- Wraps V2Ray core capabilities
- Auto-selects best transport
- Fallback to alternate protocol if connection fails

---

### 3. Connection Manager Module

**Responsibility:** Manage connection lifecycle

**States:**
```
DISCONNECTED → CONNECTING → CONNECTED → DISCONNECTING → DISCONNECTED
      ↑                        │
      └────────────────────────┴─ (on error)
```

**Features:**
- Auto-reconnect with exponential backoff
- Connection timeout handling
- Keep-alive mechanisms
- Traffic monitoring

**Interface:**
```typescript
interface ConnectionManager {
  connect(profile: Profile): Promise<Connection>;
  disconnect(): Promise<void>;
  isConnected(): boolean;
  getConnectionStatus(): ConnectionStatus;
  getCurrentServer(): ServerInfo;
  getMetrics(): {
    uploadSpeed: number;
    downloadSpeed: number;
    latency: number;
    dataUsed: number;
  };
}
```

---

### 4. Server Manager Module

**Responsibility:** Server list management, latency detection

**Features:**
- Fetch server list from subscription
- Ping each server (latency test)
- Calculate best server based on:
  - Latency (primary)
  - Server load (secondary)
  - Geographic preference (tertiary)
- Cache server list locally

**Interface:**
```typescript
interface ServerManager {
  loadServers(source: string): Promise<Server[]>;
  pingServer(server: Server): Promise<number>;
  getBestServer(region?: string): Promise<Server>;
  saveServerList(servers: Server[]): void;
  getServerList(): Server[];
  getServerDetails(serverID: string): ServerInfo;
}

interface Server {
  id: string;
  name: string;
  address: string;
  country: string;
  region: string;
  latitude: number;
  longitude: number;
  protocols: string[];
  latency: number;
  load: number;
  lastPinged: number;
}
```

**Latency Algorithm:**
```
score = (latency * 0.6) + (load * 0.3) + (stability * 0.1)
Select server with lowest score
```

---

### 5. Profile Module

**Responsibility:** Multiple configuration profiles management

**Features:**
- Create/read/update/delete profiles
- Profile cloning
- Profile favorites
- Profile metadata (name, description, icon)
- Auto-select last used profile

**Data Structure:**
```json
{
  "profiles": [
    {
      "id": "uuid",
      "name": "Work",
      "description": "For office network",
      "serverID": "jp-tokyo-01",
      "protocol": "vless",
      "dnsProvider": "cloudflare",
      "killSwitch": true,
      "splitTunneling": false,
      "lastUsed": 1705316400000,
      "isFavorite": true,
      "createdAt": 1704800000000
    }
  ]
}
```

**Interface:**
```typescript
interface ProfileManager {
  createProfile(profile: ProfileInput): Promise<Profile>;
  updateProfile(id: string, changes: Partial<Profile>): Promise<Profile>;
  deleteProfile(id: string): Promise<void>;
  getProfile(id: string): Profile;
  getAllProfiles(): Profile[];
  cloneProfile(id: string, newName: string): Profile;
  setFavorite(id: string, isFavorite: boolean): void;
  setLastUsed(id: string): void;
}
```

---

### 6. DNS Management Module

**Responsibility:** DNS configuration and privacy

**Supported Providers:**
- Cloudflare (1.1.1.1)
- Quad9 (9.9.9.9)
- Mullvad DNS
- NextDNS
- Custom providers

**DNS Protocols:**
- DoH (DNS over HTTPS)
- DoT (DNS over TLS)
- Plain DNS

**Features:**
- DNS leak testing
- Custom DNS server support
- System DNS vs Custom DNS toggle
- Per-profile DNS override

**Interface:**
```typescript
interface DNSManager {
  setDNSProvider(provider: DNSProvider): Promise<void>;
  setDNSProtocol(protocol: 'doh' | 'dot' | 'plain'): void;
  testDNSLeaks(): Promise<DNSLeakTest>;
  getCurrentDNS(): DNSSettings;
  setCustomDNS(ip: string, port: number): void;
  resolveDomain(domain: string): Promise<string[]>;
}

interface DNSProvider {
  name: string;
  dohURL?: string;
  dotServer?: string;
  plainServers?: string[];
  description: string;
  privacyPolicy: string;
}
```

---

### 7. Statistics & Analytics Module

**Responsibility:** Traffic monitoring and historical data

**Metrics Tracked:**
- Real-time upload/download speed
- Current session data usage
- Daily/weekly/monthly usage
- Connection success rate
- Most-used servers
- Most-used protocols

**Storage:**
- SQLite local database
- In-memory cache for real-time stats
- Aggregated daily summaries

**Interface:**
```typescript
interface StatsManager {
  startSession(): void;
  endSession(): void;
  recordDataUsage(bytes: number, direction: 'up' | 'down'): void;
  recordSpeed(uploadMbps: number, downloadMbps: number): void;
  getSessionStats(): SessionStats;
  getDailyStats(date: Date): DailyStats;
  getMonthlyStats(year: number, month: number): MonthlyStats;
  clearStats(beforeDate?: Date): void;
  exportStats(format: 'csv' | 'json'): string;
}

interface SessionStats {
  startTime: number;
  endTime?: number;
  duration: number;
  dataUp: number;
  dataDown: number;
  avgUploadSpeed: number;
  avgDownloadSpeed: number;
  server: ServerInfo;
  protocol: string;
}
```

---

### 8. Security Module

**Responsibility:** Encryption, secure storage, memory protection

**Features:**
- Encrypt stored credentials locally
- Secure random key generation
- Session memory protection (wipe on disconnect)
- Secure deletion of logs
- No data persistence to disk by default

**Implementation:**
```typescript
interface SecurityManager {
  encryptConfig(config: any, password: string): string;
  decryptConfig(encrypted: string, password: string): any;
  generateSecureUUID(): string;
  wipeMemory(data: Buffer): void;
  clearSensitiveData(): void;
  validateCertificate(cert: string): boolean;
}

// Configuration Encryption (AES-256-GCM)
const encrypted = securityManager.encryptConfig(config, masterPassword);
// Stored in: ~/.franvpn/profiles.encrypted
```

---

### 9. Logging Module

**Responsibility:** User-friendly, privacy-safe connection logs

**Logged Information:**
- Connection timestamp
- Server & protocol used
- Session duration
- Data transferred
- Success/failure status

**NOT Logged:**
- Traffic content
- User credentials
- DNS queries (by default)
- IP addresses of visited sites

**Interface:**
```typescript
interface LogManager {
  addConnectionLog(entry: ConnectionLogEntry): void;
  getConnectionLogs(filter?: LogFilter): ConnectionLogEntry[];
  clearLogs(beforeDate?: Date): void;
  exportLogs(format: 'csv' | 'json' | 'pdf'): string;
  getLogStats(): {
    totalConnections: number;
    successRate: number;
    averageDuration: number;
  };
}

interface ConnectionLogEntry {
  id: string;
  timestamp: number;
  server: ServerInfo;
  protocol: string;
  duration: number;
  dataUsed: { up: number; down: number };
  status: 'success' | 'timeout' | 'auth_failed' | 'unknown_error';
  errorMessage?: string;
}
```

---

### 10. Notification Module

**Responsibility:** User notifications for important events

**Events:**
- Connection established
- Connection failed
- Connection dropped
- Kill switch activated
- Server offline
- DNS leak detected

**Interface:**
```typescript
interface NotificationManager {
  notify(type: NotificationType, message: string, options?: NotificationOptions): void;
  enableNotifications(enabled: boolean): void;
  setSoundEnabled(enabled: boolean): void;
  clearNotifications(): void;
}

enum NotificationType {
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning',
  INFO = 'info'
}
```

---

## V2Ray Core Integration

### Embedding V2Ray

**Approach:** V2Ray runs as an embedded subprocess, not a library

**Advantages:**
- Process isolation
- Easier updates
- Stable interface
- Memory management

**Architecture:**
```
FRANVPN App ←IPC→ V2Ray Process (stdio/socket)
  ↓
  Generates V2Ray config JSON
  Passes via stdin or config file
  Monitors stdout/stderr
  Communicates via API port (localhost:8118)
```

**Configuration Generation:**

```typescript
function generateV2RayConfig(profile: Profile): V2RayConfig {
  return {
    inbounds: [{
      port: 10808,
      protocol: 'socks',
      settings: { auth: 'noauth' },
      sniffing: {
        enabled: true,
        destOverride: ['http', 'tls']
      }
    }],
    outbounds: [{
      protocol: profile.protocol,
      settings: {
        // Protocol-specific settings
      },
      streamSettings: {
        network: profile.transport,
        // Transport-specific settings
      },
      mux: {
        enabled: true
      }
    }],
    dns: {
      servers: [
        profile.dnsProvider.ip,
        'localhost'
      ]
    },
    routing: {
      // Advanced routing rules
    }
  };
}
```

### V2Ray Process Management

```typescript
class V2RayManager {
  private process: ChildProcess;
  
  async start(config: V2RayConfig): Promise<void> {
    // Write config to temp file
    const configPath = await fs.writeFile(config);
    
    // Start V2Ray process
    this.process = spawn('v2ray', ['-config', configPath]);
    
    // Monitor process
    this.process.on('error', this.handleError);
    this.process.stderr.on('data', this.logOutput);
    
    // Wait for API to be ready
    await this.waitForAPIReady();
  }
  
  async stop(): Promise<void> {
    this.process.kill('SIGTERM');
    await this.waitForExit();
  }
  
  async getStats(): Promise<V2RayStats> {
    // Query API port for real-time stats
    const response = await fetch('http://127.0.0.1:8118/stats');
    return response.json();
  }
}
```

---

## Platform-Specific Implementation

### Windows (Tauri + WinTun)

**VPN Stack:** WinTun (kernel driver)

```rust
// Rust backend
use wintun::Session;

#[tauri::command]
async fn setup_vpn(config: V2RayConfig) -> Result<()> {
  let adapter = wintun::Adapter::create("FRANVPN")?;
  let session = adapter.start_session()?;
  
  // Configure routing
  setup_route_table()?;
  
  Ok(())
}
```

### macOS (Tauri + Network Extension)

**VPN Stack:** Network Extension (system VPN)

```swift
// Swift backend
import NetworkExtension

func setupVPN() {
  let config = NEVPNProtocolIKEv2()
  let manager = NEVPNManager.shared()
  manager.protocolConfiguration = config
  try? manager.saveToPreferences()
  try? manager.loadFromPreferences()
  try? manager.connection.startVPNTunnel()
}
```

### Linux (Tauri + netlink/tun)

**VPN Stack:** TUN device + netlink routing

```rust
// Rust backend
use tun::Device;

#[tauri::command]
async fn setup_vpn() -> Result<()> {
  let mut config = tun::Configuration::default();
  config.name("franvpn0").address("10.0.0.2");
  
  let device = tun::create(&config)?;
  setup_route_table(device)?;
  Ok(())
}
```

### iOS (Flutter + NEVPNManager)

```dart
// Flutter
import 'package:flutter/services.dart';

class VPNService {
  static const platform = MethodChannel('com.franvpn.vpn/service');
  
  Future<void> startVPN(String configJson) async {
    try {
      await platform.invokeMethod('startVPN', {'config': configJson});
    } catch (e) {
      print('Failed to start VPN: $e');
    }
  }
}
```

### Android (Flutter + VpnService API)

```kotlin
// Kotlin backend service
class FranVPNService : VpnService() {
  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val builder = Builder()
    builder.setSession("FRANVPN")
      .addAddress("10.0.0.2", 32)
      .addRoute("0.0.0.0", 0)
      .addDnsServer("8.8.8.8")
    
    val vpn = builder.establish() ?: return START_STICKY
    return START_STICKY
  }
}
```

---

## Data Storage Architecture

### Local Storage Structure

```
~/.franvpn/
├── config.json              # Main app config
├── profiles.db              # SQLite profiles database
├── logs.db                  # SQLite connection logs
├── statistics.db            # SQLite usage statistics
├── v2ray-core              # V2Ray binary
└── certs/                   # Local certificates (optional)
```

### Database Schema

**Profiles Table:**
```sql
CREATE TABLE profiles (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  server_id TEXT,
  protocol TEXT,
  transport TEXT,
  dns_provider TEXT,
  kill_switch BOOLEAN,
  created_at INTEGER,
  updated_at INTEGER
);
```

**Connection Logs Table:**
```sql
CREATE TABLE connection_logs (
  id TEXT PRIMARY KEY,
  timestamp INTEGER,
  server_id TEXT,
  protocol TEXT,
  duration INTEGER,
  data_up INTEGER,
  data_down INTEGER,
  status TEXT,
  error_message TEXT
);
```

**Statistics Table:**
```sql
CREATE TABLE daily_stats (
  date TEXT PRIMARY KEY,
  total_data_up INTEGER,
  total_data_down INTEGER,
  connections_count INTEGER,
  success_count INTEGER
);
```

---

## Performance & Optimization

### Optimization Strategies

1. **Connection Caching:**
   - Cache active connections
   - Reuse TCP connections
   - Connection pooling

2. **Memory Management:**
   - Lazy loading of server lists
   - In-memory cache of frequently accessed data
   - Automatic memory cleanup

3. **Network Optimization:**
   - Parallel server pinging
   - Non-blocking I/O
   - Compressed data transfer

4. **UI Performance:**
   - Virtual scrolling for large lists
   - Debounced search
   - Memoized components (React)

### Benchmarks

**Target Metrics:**
- Connection time: < 5 seconds
- Server list load: < 1 second
- Profile switch: < 500ms
- UI responsiveness: 60 FPS
- Memory usage: < 100MB (idle), < 200MB (active)
- Binary size: < 50MB (desktop), < 30MB (mobile)

---

## Testing Strategy

### Unit Tests
- Protocol validation
- Configuration parsing
- Algorithm correctness

### Integration Tests
- Connection lifecycle
- Profile management
- Data persistence

### E2E Tests
- Full connection flow
- Server switching
- Profile switching

### Platform Tests
- Windows-specific networking
- macOS permissions
- Linux distribution compatibility
- iOS VPN extension
- Android VPN service

---

## Security Considerations

1. **No Logging:** Don't log traffic content
2. **Encryption:** All configs encrypted at rest
3. **Memory:** Sensitive data wiped from memory
4. **Validation:** Validate all user inputs
5. **Dependencies:** Regular security audits
6. **Updates:** Secure update mechanism

---

## Deployment & Distribution

### Desktop

**Windows:**
- MSIX package (Microsoft Store)
- Portable ZIP
- Installer (.exe)

**macOS:**
- DMG package (notarized)
- Homebrew (brew install franvpn)

**Linux:**
- .deb (Debian/Ubuntu)
- .rpm (RedHat/Fedora)
- Snapcraft
- Flatpak

### Mobile

**iOS:**
- TestFlight beta
- App Store release
- Enterprise distribution

**Android:**
- Google Play Store
- F-Droid repository
- Direct APK download

---

**Document Version:** 1.0  
**Last Updated:** January 15, 2026  
**Status:** Ready for Development
