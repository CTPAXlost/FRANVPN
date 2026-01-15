# FRANVPN – Feature Specification

## Feature Categories

---

## 1. CORE CONNECTIVITY FEATURES

### 1.1 One-Click Connect / Disconnect

**Purpose:** Simplest possible user action to establish secure connection

**Behavior:**
- Large, prominent toggle button on main dashboard
- Status transitions: Disconnected → Connecting → Connected → Disconnecting
- Estimated connection time display (2-5 seconds for most users)
- Real-time status indicator (dot animation)
- Last connected server auto-saved for quick reconnection

**Implementation:**
- Keyboard shortcut: `Ctrl+Shift+V` (quick toggle)
- System tray integration (left-click = toggle)
- Haptic feedback on mobile

**Success Criteria:**
- One-tap connection < 5 seconds
- 99.9% uptime
- No crashes on connect/disconnect

---

### 1.2 Automatic Best Server Selection

**Purpose:** Users don't need to manually choose servers; app picks optimal one

**Algorithm:**
1. Ping all available servers (staggered, non-blocking)
2. Calculate score: (latency × weight) + (load × 0.3) + (stability × 0.2)
3. Select top server for region
4. Refresh every 5 minutes in background

**Display:**
- "Auto Best" badge on dashboard
- Shows "Connecting to [Server Name] ([Country], [Latency]ms)"
- User can see why auto-selection chose this server

**Advanced:**
- User-defined server prioritization (prefer nearby regions)
- Exclude servers with high packet loss
- Consider time-of-day load patterns

**Success Criteria:**
- Server selection < 3 seconds
- Latency accuracy within ±15ms
- Smart refresh without disrupting active connections

---

### 1.3 Manual Server & Protocol Selection (Advanced Mode)

**Purpose:** Power users need fine-grained control

**Features:**

#### Server Selection
- Interactive world map with server locations
- Server list view (sortable by latency, location, load)
- Favorite servers (pin/star system)
- Server filters (region, protocol, load, last used)
- Detailed server info (IP, operator, security certificates)

#### Protocol Selection
- Dropdown menu: VLESS, VMess, Trojan, Shadowsocks, SOCKS5, HTTP/HTTPS
- Protocol-specific options:
  - **VLESS:** UUID, encryption level (auto/none/zero)
  - **VMess:** UUID, encryption (auto/aes-128-gcm)
  - **Trojan:** Password, allowInsecure toggle
  - **Shadowsocks:** Method (aes-256-gcm, chacha20-poly1305)
  - **Transports:** WebSocket, gRPC, QUIC (with specific settings)
- TLS/Reality/XTLS options display

#### Connection Preview
- Connection diagram showing: Client → Protocol → Transport → Server
- Estimated performance metrics (before connecting)
- Security assessment badge (strong/standard/custom)

**Success Criteria:**
- Power users complete setup in < 30 seconds
- All protocol options accessible within 2 taps
- Visual clarity on connection path

---

## 2. CONFIGURATION MANAGEMENT

### 2.1 Profile-Based Configuration

**Purpose:** Users maintain multiple accounts/configs for different use cases

**Features:**
- Create unlimited profiles
- Each profile contains:
  - Name & description
  - Selected server & protocol
  - Protocol-specific settings
  - DNS settings
  - Kill switch preference
  - Split tunneling rules
  - Connection logs

**Profile Types:**
- **Default:** Auto-created, can't delete
- **Custom:** User-created
- **Imported:** From QR code, subscription, or file

**Profile Management:**
- Rename, duplicate, delete, reorder
- Mark as "Favorite" for quick access
- Last-used timestamp
- Connection statistics per profile
- Cloud sync option (Pro feature)

**UI Representation:**
- Profile switcher on main dashboard
- Profile card showing: name, server, protocol, last used
- Swipe to delete on mobile
- Right-click context menu on desktop

**Success Criteria:**
- Switch between profiles in < 2 seconds
- Support 50+ active profiles without performance degradation
- Cloud sync < 1 second

---

### 2.2 QR Code Import & Export

**Purpose:** Frictionless sharing of configurations

**Import Process:**
1. Open "Import Config" → "Scan QR Code"
2. Point camera at QR code
3. Auto-detect format (V2Ray, subscription, etc.)
4. Show preview: "Add profile 'Work VPN'?"
5. Confirm → Profile added

**Export Process:**
1. Open profile → "Share"
2. Show QR code (with option to save as image)
3. Auto-copy to clipboard
4. Share via messaging apps

**Supported Formats:**
- V2Ray JSON configs
- VLESS links (`vless://...`)
- VMess links (`vmess://...`)
- Trojan links
- Shadowsocks links
- Subscription URLs

**QR Code Features:**
- Error-corrected for low-light scanning
- Size adjustable in preview
- Animated QR rotation on mobile for better scanning

**Success Criteria:**
- QR scan succeeds 98%+ first attempt
- Config import < 2 seconds
- Support all V2Ray formats + custom formats

---

### 2.3 Subscription Link Support

**Purpose:** Auto-update server list from subscription providers

**Features:**
- Paste subscription URL
- Auto-fetch profile updates (on-demand or scheduled)
- Show update timestamp
- Maintain local overrides (server selections, DNS)

**Subscription Format Support:**
- V2Ray subscription (custom JSON)
- Clash config format
- Standard subscription format

**Behavior:**
- Manual refresh button
- Auto-refresh toggle (update every 1-24 hours)
- Notification when new servers available
- Local fallback if update fails
- Show diff of new/removed servers

**Pro Feature:** Cloud-sync subscriptions across devices

**Success Criteria:**
- Subscription parsing < 1 second
- 99.9% update reliability
- Support major providers (user subscriptions + custom)

---

## 3. ADVANCED SECURITY FEATURES

### 3.1 Kill Switch

**Purpose:** Immediately stop all traffic if VPN connection drops

**Behavior:**
- When enabled: Internet access = VPN only (no fallback)
- If VPN drops: All non-VPN traffic blocked
- Manual disconnect: Restore normal internet

**Implementation:**
- **Windows:** Firewall rules
- **macOS:** PF rules
- **Linux:** iptables
- **iOS:** VPN extension architecture
- **Android:** VpnService API

**Settings:**
- Toggle on/off
- Toggle per-app (which apps affected)
- Exception list (apps/ports allowed even without VPN)

**Notifications:**
- Alert when Kill Switch blocks traffic
- Show disruption time
- "Reconnecting..." status

**Success Criteria:**
- Enable/disable < 100ms
- Zero traffic leakage in tests
- Minimal performance impact

---

### 3.2 Split Tunneling

**Purpose:** Route some traffic through VPN, other traffic directly

**Features:**

#### App-Level Tunneling (Desktop & Mobile)
- List of installed apps
- Toggle each app: "Route through VPN" or "Direct"
- Preset profiles: "Streaming" (exclude), "Work" (include specific), "Full" (all VPN)

#### Domain-Level Tunneling (Advanced)
- Custom list of domains to exclude from VPN
- Regex support for advanced users
- Common presets:
  - Banking apps (exclude from VPN for compliance)
  - Streaming services (exclude to use local IP)
  - Work apps (require VPN)

**UI:**
- Simple view: Toggle switches per app
- Advanced view: Regex editor
- Search function across apps

**Performance:**
- Real-time traffic classification
- < 5% performance overhead
- Smart rule caching

**Success Criteria:**
- Rule updates < 500ms
- Support 200+ app rules
- Zero traffic misrouting

---

### 3.3 DNS Management

**Purpose:** Control DNS resolution for privacy and security

**Features:**

#### DNS Provider Selection
- Built-in options:
  - Cloudflare (1.1.1.1) – Default
  - Quad9 (9.9.9.9)
  - NextDNS
  - Mullvad DNS
  - Custom option

#### DNS Protocols
- **DoH (DNS over HTTPS)** – Encrypted DNS
- **DoT (DNS over TLS)** – Encrypted DNS
- **Plain DNS** – Standard (only over VPN)

#### Advanced Options
- Custom DNS server (IP:port)
- DNS leakage test (verify no DNS leaks)
- Fake IP mode (for gRPC transport)

#### DNS Leak Testing
- Built-in test button: "Check DNS Leaks"
- Shows resolved IP, location, ISP
- Alert if leaks detected

**Settings:**
- Toggle "System DNS" vs "Custom DNS"
- Per-profile DNS override
- Auto-update of provider lists

**Success Criteria:**
- DNS response < 200ms
- Zero DNS leaks
- Support DoH/DoT providers

---

## 4. MONITORING & ANALYTICS

### 4.1 Traffic Statistics (Real-time & Historical)

**Real-Time Display:**
- Current upload/download speed (animated)
- Data transferred (current session)
- Connected time (duration)
- Current server & location
- Current IP address

**Historical Dashboard:**
- Daily/weekly/monthly breakdown
- Total data transferred (all time)
- Most-used servers
- Most-used protocols
- Usage by time-of-day heatmap
- Connection stability graph (uptime %)

**Data Visualization:**
- Animated speed gauge (0-100 Mbps)
- Stacked bar charts (daily usage)
- Line graph (connection history over 30 days)
- Pie chart (server distribution)

**Export Options:**
- Export stats as CSV
- Monthly summary email (Pro feature)

**Privacy:**
- All stats stored locally
- Never sent to servers
- User can clear history anytime

**Success Criteria:**
- Real-time updates < 1 second latency
- Graphs render < 500ms
- Support 12+ months of history

---

### 4.2 Connection Logs

**Purpose:** User-friendly, privacy-safe connection history

**Log Entries Display:**
- Timestamp (relative: "2 hours ago")
- Connected server & country
- Protocol used
- Session duration
- Data transferred
- Connection status (success/timeout/auth failed)

**Filtering & Search:**
- Filter by date range
- Filter by server/protocol
- Search by country
- Show only failed connections
- Show only successful connections

**Privacy Features:**
- Minimal data retention (default 30 days, user-adjustable)
- One-tap "Clear All Logs"
- Logs encrypted locally
- Logs never synced to cloud (unless user opts in)

**Detailed View:**
- Click log entry to see:
  - Full server details
  - Protocol configuration (redacted sensitive data)
  - Connection time breakdown
  - Failure reason (if failed)

**Export:**
- Export logs as PDF
- Share specific log entry

**Success Criteria:**
- Log history load < 500ms
- Support 1000+ log entries
- Search < 100ms

---

## 5. IMPORT / EXPORT

### 5.1 Config File Import

**Supported Formats:**
- V2Ray JSON config files
- Clash YAML configs
- Binary files (vmess:// etc.)

**Import Process:**
1. "Import Config" button
2. File browser or drag-and-drop
3. Auto-parse and validate
4. Show preview
5. Create new profile from config

**Error Handling:**
- Clear error messages
- Suggestions for fixing invalid configs
- Rollback on failed import

**Success Criteria:**
- Parse valid configs 100%
- Clear error messages for invalid configs
- Support V2Ray 4.x+ format

---

### 5.2 Config File Export

**Export Options:**
- Export as V2Ray JSON (full config)
- Export as V2Ray link (vless://)
- Export as QR code

**What's Exported:**
- Server info (IP, port, protocol)
- Protocol settings
- TLS/REALITY settings
- Transport settings

**What's NOT Exported (Security):**
- DNS settings (user preference)
- Kill switch settings (device-specific)
- Split tunneling rules (device-specific)

**File Format:**
- Standard V2Ray JSON structure
- Clear comments in exported JSON

---

## 6. USER EXPERIENCE FEATURES

### 6.1 Simple Mode (Beginner-Friendly)

**Target:** Non-technical users who want "just connect"

**Display:**
- Large "Connect" button (primary)
- Current status at top
- Current IP & location
- "Change Server" button (shows clean list)
- Settings button (minimal options)

**Hidden Complexity:**
- Protocol selection hidden
- Advanced protocol options hidden
- No technical terminology

**Available Actions:**
- Connect/Disconnect
- Change server
- Change DNS provider (simple list)
- Toggle Kill Switch
- View statistics (basic)
- Access help/support

**Success Criteria:**
- Non-technical user can connect in 3 taps
- No technical jargon visible
- 90%+ task completion for beginners

---

### 6.2 Advanced Mode (Power Users)

**Target:** Users who need protocol control, debugging, customization

**Available Features:**
- Full protocol selection & customization
- Raw config import/export
- Connection logs with full details
- DNS leak testing
- Speed testing
- Traffic classification
- Custom routing rules
- Performance profiling

**UI Enhancements:**
- Advanced settings panel
- Raw JSON config viewer
- Detailed diagnostics
- Hidden technical options become visible

**Success Criteria:**
- All power user features accessible
- No UI clutter for basic users
- Smooth toggle between modes

---

## 7. SYSTEM INTEGRATION

### 7.1 System Tray Integration

**Features:**
- Click tray icon to toggle connection
- Right-click for menu:
  - Quick connect
  - Recent servers
  - Settings
  - Exit
- Status indicator (green = connected, gray = disconnected)
- Tooltip showing current IP

**Platforms:**
- Windows: System tray
- macOS: Menu bar
- Linux: Varies (GNOME, KDE support)

---

### 7.2 Keyboard Shortcuts

**Global Shortcuts (All Platforms):**
- `Ctrl+Shift+V` (Windows/Linux) / `Cmd+Shift+V` (macOS): Toggle connection
- `Ctrl+Shift+S` (Windows/Linux) / `Cmd+Shift+S` (macOS): Quick settings
- `Ctrl+Shift+C` (Windows/Linux) / `Cmd+Shift+C` (macOS): Open connection panel

**In-App Shortcuts:**
- `Esc`: Close dialogs
- `↑/↓`: Navigate server list
- `Enter`: Connect to selected server
- `Ctrl+Q`: Exit application

---

### 7.3 Notifications

**Connection Notifications:**
- Connection successful (optional)
- Connection failed (required)
- Disconnected unexpectedly (alert)
- Kill switch activated (alert)
- Server updated (optional)

**Settings:**
- Toggle notifications per event type
- Sound on/off
- Toast vs. banner

---

## 8. SUPPORT & HELP

### 8.1 In-App Help

**Features:**
- Contextual help tooltips
- Video tutorials
- FAQ section
- Glossary (explain technical terms)
- Settings search (find any setting)

---

### 8.2 Diagnostic Tools

**Available Tools:**
- Connection test (try connection without saving)
- Speed test (measure actual speed)
- DNS leak test
- IP leak test
- Protocol compatibility test
- System diagnostics

**Export Diagnostics:**
- One-button crash report
- Automatic error log export
- Sensitive data redaction

---

## Feature Priority (MVP to Pro)

### MVP (Phase 1)
- ✅ One-click connect/disconnect
- ✅ Auto-best server selection
- ✅ Manual server selection
- ✅ Manual protocol selection
- ✅ Profile management
- ✅ QR code import
- ✅ Basic statistics
- ✅ Simple mode UI
- ✅ System tray
- ✅ Keyboard shortcuts

### Phase 2
- ✅ Kill switch
- ✅ Split tunneling
- ✅ DNS management
- ✅ Advanced mode
- ✅ Connection logs
- ✅ Subscription link support
- ✅ Config file import/export
- ✅ Speed testing

### Phase 3 (Pro)
- ✅ Cloud sync
- ✅ Monthly stats email
- ✅ Priority support
- ✅ Custom DNS profiles
- ✅ Advanced split tunneling (regex)
- ✅ Multiple device support

---

**Document Version:** 1.0  
**Last Updated:** January 15, 2026  
**Status:** Ready for Development
