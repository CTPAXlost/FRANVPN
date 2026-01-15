# FRANVPN – UI/UX Design Specification

---

## Design Philosophy

**Core Principles:**
1. **Clarity First** – Status, metrics, and actions are instantly clear
2. **Progressive Disclosure** – Advanced options available but not overwhelming
3. **Confidence Through Design** – Professional, premium aesthetic builds trust
4. **Global Visual Language** – Icons, colors, and patterns consistent across platforms
5. **Dark-First Design** – Optimized for dark theme, light theme equally polished

---

## Screen Navigation Architecture

### Desktop Client (Windows/macOS/Linux)

```
┌─ Main Window (800x600px minimum)
│
├─ Dashboard (Default View)
│  ├─ Connection Status Panel
│  ├─ Server & Protocol Info
│  ├─ Network Statistics
│  └─ Quick Actions
│
├─ Server Selection Panel
│  ├─ Auto-Best Server Display
│  ├─ Map View
│  ├─ List View
│  ├─ Server Details
│  └─ Favorites
│
├─ Profiles Panel
│  ├─ Profile List
│  ├─ Create/Edit Profile
│  ├─ Import Profile
│  └─ Profile Options
│
├─ Settings Panel
│  ├─ Connection Settings
│  ├─ DNS Management
│  ├─ Kill Switch / Split Tunneling
│  ├─ Appearance
│  └─ Advanced Options
│
└─ Support Panel
   ├─ Help & Documentation
   ├─ Diagnostics
   ├─ About & Version
   └─ Contact Support
```

### Mobile App (iOS/Android)

```
┌─ Tab Navigation (Bottom Tabs)
│
├─ Home Tab
│  ├─ Connection Status (Hero Section)
│  ├─ Current IP & Location
│  ├─ Quick Server Switch
│  └─ Recent Servers
│
├─ Servers Tab
│  ├─ Map View (iOS Swipeable)
│  ├─ List View (Filterable)
│  ├─ Search
│  └─ Server Details Modal
│
├─ Profiles Tab
│  ├─ Profile List
│  ├─ Create New Profile
│  ├─ Import Profile
│  └─ Profile Details
│
└─ Settings Tab
   ├─ Appearance
   ├─ Connection Settings
   ├─ Security Options
   ├─ About
   └─ Help
```

---

## Screen-by-Screen Design Specs

### 1. DASHBOARD (Main Screen)

**Desktop Layout:**

```
┌─────────────────────────────────────────────────┐
│  [Logo] FRANVPN          [User] [Settings] [?]  │
├─────────────────────────────────────────────────┤
│                                                 │
│        ┌─────────────────────────────┐          │
│        │                             │          │
│        │     [BIG TOGGLE BUTTON]     │          │
│        │      🔓 DISCONNECTED        │          │
│        │                             │          │
│        └─────────────────────────────┘          │
│                                                 │
│  STATUS SECTION:                                │
│  ├─ 📍 Connected Server: Auto Best (Tokyo)      │
│  ├─ 🔒 Protocol: VLESS + gRPC                   │
│  └─ ⏱ Connected: 2 hours 15 min                 │
│                                                 │
│  NETWORK SECTION:                               │
│  ├─ Upload: ↑ 2.4 MB/s    Download: ↓ 45 MB/s │
│  ├─ IP Address: 203.0.113.42 (Japan)           │
│  └─ 📊 Data Used: 1.2 GB / 50 GB (session)    │
│                                                 │
│  QUICK ACTIONS:                                 │
│  ├─ [Change Server]  [Statistics]  [Details]   │
│                                                 │
└─────────────────────────────────────────────────┘
```

**Key Elements:**
- **Hero Button:** 200x200px, center screen, high contrast color
  - State 1 (Disconnected): Gray, "Tap to Connect"
  - State 2 (Connecting): Blue with spinner animation
  - State 3 (Connected): Green with lock icon
  - State 4 (Disconnecting): Animated transition
  
- **Status Display:**
  - Server name, country flag, latency (e.g., "Tokyo – 28ms")
  - Protocol name with badge (e.g., "VLESS + gRPC")
  - Connected duration with clock icon
  
- **Network Stats:**
  - Real-time animated speed display (gauge or numbers)
  - Current public IP with geolocation
  - Data transferred (session/daily)
  
- **Visual Hierarchy:**
  - Toggle button = 60% visual weight
  - Status = 25% visual weight
  - Stats = 15% visual weight

**Mobile Layout:**
- Full-width hero button (90% viewport width)
- Stacked elements below
- Swipe down to reveal more details
- Bottom action sheet for quick server change

---

### 2. SERVER SELECTION SCREEN

**Desktop – List View:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    Servers    [Search...] [Map View]    │
├─────────────────────────────────────────────────┤
│                                                 │
│  FILTER BAR:                                    │
│  [All Regions ▼] [All Protocols ▼] [Clear]     │
│                                                 │
│  AUTO BEST (Current Selection) ⭐               │
│  ├─ 🌍 Japan – Tokyo – 28ms – 15% Load        │
│  ├─ Last used: 5 min ago                       │
│  └─ [Details]                                   │
│                                                 │
│  FAVORITES:                                     │
│  ├─ 🌍 USA – New York – 85ms – ⭐              │
│  ├─ 🌍 Germany – Berlin – 42ms – ⭐            │
│                                                 │
│  ALL SERVERS:                                   │
│  ├─ 🌍 Singapore – 12ms – 5% Load              │
│  ├─ 🌍 Australia – Sydney – 120ms – 8% Load   │
│  ├─ 🌍 Canada – Toronto – 95ms – 12% Load     │
│  ├─ 🌍 France – Paris – 38ms – 20% Load       │
│  ├─ 🌍 India – Mumbai – 65ms – 25% Load       │
│  └─ ... (more servers)                         │
│                                                 │
│  [✓ Connect to Selected Server]                 │
└─────────────────────────────────────────────────┘
```

**Desktop – Map View:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    Servers    [List View] [Search]     │
├─────────────────────────────────────────────────┤
│                                                 │
│     [Interactive World Map]                     │
│                                                 │
│     🟢 Clickable server markers                │
│     Color coded by latency:                     │
│     🟢 Green: < 50ms                           │
│     🟡 Yellow: 50-100ms                        │
│     🔴 Red: > 100ms                            │
│                                                 │
│     Hover tooltip: "Tokyo – 28ms – VLESS"      │
│                                                 │
└─────────────────────────────────────────────────┘
```

**Server Detail Card (Click Detail):**

```
┌─────────────────────────────────────────────────┐
│  Japan – Tokyo                              [X] │
├─────────────────────────────────────────────────┤
│  Server Information:                            │
│  ├─ IP Address: 203.0.113.100                   │
│  ├─ Port: 443                                   │
│  ├─ Latency: 28ms (estimated)                   │
│  ├─ Load: 15%                                   │
│  └─ Last Used: 5 min ago                        │
│                                                 │
│  Available Protocols:                           │
│  ├─ ✓ VLESS (Recommended)                       │
│  ├─ ✓ Trojan                                    │
│  ├─ ✓ Shadowsocks                               │
│                                                 │
│  Transport Options:                             │
│  ├─ WebSocket ✓                                 │
│  ├─ gRPC ✓                                      │
│  └─ QUIC ✓                                      │
│                                                 │
│  [⭐ Add to Favorites] [✓ Connect]              │
└─────────────────────────────────────────────────┘
```

**Mobile Server List:**
- Full-width server cards
- Swipe left to favorite/remove
- Tap to see details
- Pull down to refresh

---

### 3. PROTOCOL SELECTION SCREEN (Advanced Mode)

**Layout:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    Protocol Settings                    │
├─────────────────────────────────────────────────┤
│                                                 │
│  PROTOCOL: [VLESS ▼]                            │
│                                                 │
│  ┌─ VLESS Configuration                         │
│  │                                              │
│  │  UUID:                                       │
│  │  [xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx]     │
│  │                                              │
│  │  Encryption:                                 │
│  │  ◉ Auto (Recommended)                        │
│  │  ○ None                                      │
│  │  ○ Zero                                      │
│  │                                              │
│  │  Flow: [empty ▼]                             │
│  │                                              │
│  └─────────────────────────────────────────────┘
│                                                 │
│  TRANSPORT: [gRPC ▼]                            │
│                                                 │
│  ┌─ Transport Configuration                     │
│  │                                              │
│  │  Service Name:                               │
│  │  [grpc.franvpn.io]                           │
│  │                                              │
│  │  Multi Mode: ☑ Enabled                       │
│  │                                              │
│  └─────────────────────────────────────────────┘
│                                                 │
│  SECURITY: [TLS ▼]                              │
│                                                 │
│  ┌─ TLS/REALITY Configuration                   │
│  │                                              │
│  │  Server Name (SNI):                          │
│  │  [www.google.com]                            │
│  │                                              │
│  │  Allow Insecure: ☐                           │
│  │                                              │
│  │  Certificate Pinning: ☐                      │
│  │                                              │
│  └─────────────────────────────────────────────┘
│                                                 │
│  [Test Connection] [✓ Save & Connect]           │
└─────────────────────────────────────────────────┘
```

**Dynamic Content:**
- Protocol dropdown changes form below
- Each protocol shows only relevant options
- Validation in real-time (red borders for errors)
- Test button tries connection without saving

---

### 4. PROFILES SCREEN

**Desktop Layout:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    My Profiles    [+ New Profile]       │
├─────────────────────────────────────────────────┤
│                                                 │
│  PROFILE LIST:                                  │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ ⭐ Default                              │   │
│  │ Auto Best (Japan) • VLESS • Last: Now  │   │
│  │ [Edit] [Delete]                        │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 💼 Work VPN                             │   │
│  │ USA – New York • Trojan • Last: 2h ago  │   │
│  │ [Edit] [Delete]                        │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 🎬 Streaming (Split Tunneling)         │   │
│  │ UK – London • Shadowsocks • Last: 4d   │   │
│  │ [Edit] [Delete]                        │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 🌐 Personal                             │   │
│  │ Germany – Berlin • VLESS • Last: 1w ago │   │
│  │ [Edit] [Delete]                        │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  QUICK ACTIONS:                                 │
│  [Import from File] [Import from QR]            │
└─────────────────────────────────────────────────┘
```

**Edit Profile Dialog:**

```
┌─────────────────────────────────────────────────┐
│  Edit Profile: Work VPN                     [X] │
├─────────────────────────────────────────────────┤
│                                                 │
│  Profile Name:                                  │
│  [Work VPN                            ]         │
│                                                 │
│  Description (optional):                        │
│  [For daily work tasks                ]         │
│                                                 │
│  Server:                                        │
│  [USA – New York                    ▼]         │
│                                                 │
│  Protocol:                                      │
│  [Trojan                             ▼]         │
│                                                 │
│  DNS Provider:                                  │
│  [Cloudflare 1.1.1.1                ▼]         │
│                                                 │
│  Kill Switch:       ☑ Enabled                   │
│  Split Tunneling:   ☐ Disabled                  │
│                                                 │
│  [Delete Profile] ... [Cancel] [Save]          │
└─────────────────────────────────────────────────┘
```

---

### 5. SETTINGS SCREEN

**Desktop Settings Navigation:**

```
┌─────────────────────────────────────────────────┐
│  Settings                                   [X] │
├──────────────┬────────────────────────────────┤
│ SIDEBAR      │ CONTENT AREA                   │
│              │                                │
│ General ▶    │ ┌──────────────────────────┐  │
│ ├─ Appearance│ │ APPEARANCE                │  │
│ ├─ Language  │ │                          │  │
│ ├─ Startup   │ │ Theme:                   │  │
│ │            │ │ ◉ Dark (Recommended)    │  │
│ Connection ▶ │ │ ○ Light                  │  │
│ ├─ DNS       │ │ ○ Auto (System)          │  │
│ ├─ Kill Sw   │ │                          │  │
│ ├─ Split Tun │ │ Language:                │  │
│ │            │ │ [English ▼]              │  │
│ Security ▶   │ │                          │  │
│ ├─ Logs      │ │ Start on Boot: ☑         │  │
│ ├─ Privacy   │ │ Minimize to Tray: ☑     │  │
│ │            │ │                          │  │
│ About ▶      │ │ [Save]                   │  │
│ ├─ Version   │ │                          │  │
│ ├─ License   │ │ └──────────────────────────┘  │
│ └─ Support   │                                │
│              │                                │
└──────────────┴────────────────────────────────┘
```

**Settings Panels:**

**1. General / Appearance**
- Theme: Dark, Light, Auto
- Language selector
- Startup behavior (minimized/windowed)
- Minimize to tray
- System integration options

**2. Connection**
- Default connection mode (Simple/Advanced)
- Auto-connect on startup
- Default profile selection
- Inactivity timeout

**3. DNS**
- DNS provider selection (Cloudflare, Quad9, etc.)
- DNS protocol (DoH, DoT, Plain)
- Custom DNS server
- Enable/Disable DNS leak test

**4. Security**
- Kill switch toggle
- Kill switch exceptions
- Split tunneling rules
- Memory protection
- Secure clipboard

**5. Privacy & Logs**
- Log retention period (7-365 days)
- Clear logs button
- Local encryption toggle
- Analytics toggle

**6. About**
- App version
- V2Ray core version
- Check for updates
- Open source licenses
- Support & feedback

---

### 6. STATISTICS SCREEN

**Layout:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    Statistics    [Range: This Month ▼] │
├─────────────────────────────────────────────────┤
│                                                 │
│  USAGE SUMMARY:                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ Total Data:    2.4 GB / 50 GB (4.8%)   │   │
│  │ Active Time:   4h 32m (avg 1.2h/day)    │   │
│  │ Connections:   142 (avg 18/day)         │   │
│  │ Success Rate:  98.7%                    │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  DAILY USAGE CHART:                             │
│  │ 1.2 │                 ╱╲    ╱╲             │
│  │ 1.0 │    ╱╲           ╱  ╲  ╱  ╲           │
│  │ 0.8 │   ╱  ╲    ╱╲   ╱    ╲╱    ╲         │
│  │ 0.6 │  ╱    ╲   ╱  ╲╱           ╲╱      │
│  │ 0.4 │ ╱      ╲ ╱                       │
│  │     │────────────────────────────────── │
│  │     │ M  T  W  T  F  S  S  M  T  W  T  │
│  │ (GB)                                    │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  TOP SERVERS (This Month):                      │
│  1. Japan (Tokyo)     – 0.8 GB – 28ms          │
│  2. USA (New York)    – 0.6 GB – 85ms          │
│  3. Germany (Berlin)  – 0.4 GB – 42ms          │
│  4. UK (London)       – 0.3 GB – 38ms          │
│  5. Singapore         – 0.3 GB – 12ms          │
│                                                 │
│  TOP PROTOCOLS:                                 │
│  1. VLESS         – 60% (1.44 GB)              │
│  2. Trojan        – 25% (0.60 GB)              │
│  3. Shadowsocks   – 15% (0.36 GB)              │
│                                                 │
│  [Export as PDF]     [Clear Statistics]         │
└─────────────────────────────────────────────────┘
```

**Mobile Layout:**
- Card-based design
- Scrollable statistics
- Swipe between different date ranges
- Tap chart for detailed view

---

### 7. CONNECTION LOGS SCREEN

**Layout:**

```
┌─────────────────────────────────────────────────┐
│  ◄ Back    Connection Logs                      │
├─────────────────────────────────────────────────┤
│                                                 │
│  FILTERS:                                       │
│  [Show: All ▼] [Date Range: Last 30 Days ▼]   │
│  [Search...] [Clear Logs]                      │
│                                                 │
│  LOGS:                                          │
│  ┌─────────────────────────────────────────┐   │
│  │ 2 hours ago • Japan (Tokyo)  ✓ Success  │   │
│  │ VLESS + gRPC • 1h 45m • 245 MB          │   │
│  │ [Details] [Copy Details]                │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 5 hours ago • USA (New York) ✓ Success  │   │
│  │ Trojan • 2h 30m • 512 MB                │   │
│  │ [Details]                               │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 1 day ago • Germany (Berlin) ✗ Failed   │   │
│  │ Timeout after 5 seconds                 │   │
│  │ [Details] [Retry]                       │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │ 2 days ago • UK (London)     ✓ Success  │   │
│  │ Shadowsocks + WebSocket • 3h • 1.2 GB  │   │
│  │ [Details]                               │   │
│  └─────────────────────────────────────────┘   │
│                                                 │
│  [Show More]                                    │
│                                                 │
└─────────────────────────────────────────────────┘
```

**Log Detail Modal:**

```
┌─────────────────────────────────────────────────┐
│  Connection Details                         [X] │
├─────────────────────────────────────────────────┤
│                                                 │
│  Timestamp: Jan 15, 2025 – 2:30 PM             │
│  Server: Japan – Tokyo                         │
│  Status: ✓ Connected Successfully              │
│                                                 │
│  Connection Details:                            │
│  ├─ Protocol: VLESS                             │
│  ├─ Transport: gRPC                             │
│  ├─ Security: TLS 1.3                           │
│  ├─ IP Address: 203.0.113.42                    │
│  └─ Port: 443                                   │
│                                                 │
│  Session Information:                           │
│  ├─ Connected Time: 1h 45m 23s                  │
│  ├─ Data Uploaded: 54 MB                        │
│  ├─ Data Downloaded: 191 MB                     │
│  ├─ Avg Upload Speed: 8.6 kB/s                  │
│  └─ Avg Download Speed: 30.4 kB/s               │
│                                                 │
│  [Copy to Clipboard] [Export] [Close]           │
└─────────────────────────────────────────────────┘
```

---

## Mobile-Specific Layouts

### iOS/Android Home Screen

```
┌─────────────────────────────────────────────────┐
│ 🔔 ☐☐☐☐☐☐☐☐☐ 100%  9:41                       │
├─────────────────────────────────────────────────┤
│                                                 │
│  FRANVPN                                        │
│                                                 │
│        ┌──────────────────────┐                │
│        │                      │                │
│        │   [HERO TOGGLE BTN]  │                │
│        │    🔓 DISCONNECTED   │                │
│        │                      │                │
│        └──────────────────────┘                │
│                                                 │
│  IP: 203.0.113.1 • Connecting...               │
│  Server: Auto Best (Tokyo, 28ms)               │
│  Speed: ↓ 45 Mbps ↑ 2.4 Mbps                  │
│                                                 │
├─────────────────────────────────────────────────┤
│  [Change Server] [Details] [Statistics]        │
├─────────────────────────────────────────────────┤
│                                                 │
│  ✨ Premium Features                            │
│  🔒 Kill Switch is ON                          │
│  🎯 Split Tunneling Disabled                   │
│                                                 │
└─────────────────────────────────────────────────┘
 🏠 Home  🌍 Servers  👤 Profiles ⚙️ Settings
```

---

## Animation & Interaction Design

### Connection Toggle Animation
- **0ms-500ms:** Button color transition (gray → blue)
- **500ms-1000ms:** Subtle rotation of icon
- **1000ms-2000ms:** Spinner rotation at center
- **Success:** Button color green, checkmark appears

**Mobile:** Haptic feedback on tap

### Loading States
- Spinner animation (smooth 360° rotation)
- Subtle pulsing effect on status text
- Skeleton loaders for data lists

### Server Selection Transition
- Fade out current server
- Slide in new server
- Animate latency number change

### Statistics Chart Animation
- Bars grow from bottom to top (500ms duration)
- Number counter animates from 0 to final value
- Smooth line drawing for trend charts

---

## Color Palette & Visual System

### Primary Colors

| Usage | Light Mode | Dark Mode | Hex Code |
|-------|----------|-----------|----------|
| **Primary (Connected)** | #00B074 | #00C878 | Green |
| **Primary (Disconnected)** | #9CA3AF | #6B7280 | Gray |
| **Primary (Error)** | #EF4444 | #F87171 | Red |
| **Primary (Warning)** | #F59E0B | #FBBF24 | Amber |
| **Primary (Info)** | #3B82F6 | #60A5FA | Blue |

### Semantic Colors

- **Success:** Green (#00B074)
- **Error:** Red (#EF4444)
- **Warning:** Amber (#F59E0B)
- **Info:** Blue (#3B82F6)
- **Neutral:** Gray (#6B7280)

### Typography

**Font Family:** Inter (sans-serif)
- Modern, clean, professional

**Font Sizes & Weights:**
- **H1 (Page Title):** 32px, Weight 700
- **H2 (Section Title):** 24px, Weight 600
- **H3 (Card Title):** 18px, Weight 600
- **Body (Regular Text):** 14px, Weight 400
- **Body Small:** 12px, Weight 400
- **Caption:** 11px, Weight 500

**Line Height:**
- Headers: 1.2
- Body: 1.5
- Lists: 1.6

---

## Icon System

**Icon Library:** Heroicons or custom FRANVPN icon set

**Common Icons:**
- `shield` – Security, VPN active
- `lock` – Secure, encrypted
- `globe` – Servers, international
- `signal` – Connection quality, latency
- `chart-bar` – Statistics
- `gears` – Settings
- `question-mark-circle` – Help
- `bell` – Notifications
- `user` – Profile/Account

**Custom FRANVPN Icons:**
- FV logo (primary icon)
- Protocol icons (VLESS, Trojan, etc.)
- Connection status icons

---

## Spacing & Layout Grid

**Base Unit:** 8px grid

**Common Spacing:**
- **XS:** 4px
- **S:** 8px
- **M:** 16px
- **L:** 24px
- **XL:** 32px
- **2XL:** 48px

**Component Sizing:**
- **Button Height:** 44px (mobile), 40px (desktop)
- **Input Height:** 40px
- **Card Padding:** 16px
- **Screen Padding:** 16px (mobile), 24px (desktop)

---

## Responsive Design Breakpoints

- **Mobile:** < 640px
- **Tablet:** 640px – 1024px
- **Desktop:** > 1024px

**Adaptation Strategy:**
- Mobile: Stacked, full-width elements
- Tablet: 2-column layout
- Desktop: 3+ column layout, sidebar navigation

---

## Dark & Light Theme Implementation

### Dark Theme (Default)
- **Background:** #0F172A (deep navy)
- **Surface:** #1E293B (dark gray)
- **Text:** #F1F5F9 (off-white)
- **Text Secondary:** #CBD5E1 (light gray)
- **Border:** #334155 (slate)

### Light Theme
- **Background:** #FFFFFF (white)
- **Surface:** #F8FAFC (light gray)
- **Text:** #0F172A (dark navy)
- **Text Secondary:** #64748B (medium gray)
- **Border:** #E2E8F0 (light border)

**Theme Toggle:** Settings > Appearance

---

## Accessibility (WCAG 2.1 AA)

- **Contrast Ratio:** Minimum 4.5:1 for text
- **Font Size:** Minimum 12px for body text
- **Interactive Elements:** Minimum 44x44px touch target
- **Focus States:** Visible focus ring (2px solid)
- **Keyboard Navigation:** Full keyboard support
- **Screen Reader:** ARIA labels on all interactive elements

---

**Document Version:** 1.0  
**Last Updated:** January 15, 2026  
**Status:** Ready for Design System Implementation
