# Quick Start: GitHub Actions APK Builds

## ⚡ TL;DR

Your APK builds are now **fully automated** on GitHub Actions.

### Get Started in 3 Steps

#### 1️⃣ Commit Changes
```bash
cd /workspaces/FRANVPN
git add .github/ DOWNLOAD_APK.md
git commit -m "ci: Add GitHub Actions APK builds"
git push origin main
```

#### 2️⃣ Watch Build
Go to: [Actions Tab](https://github.com/YOUR_USERNAME/FRANVPN/actions)
- Wait ~5-8 minutes for build
- Find APK in "Artifacts"

#### 3️⃣ Share (Optional)
```bash
git tag v1.0.0
git push origin v1.0.0
```
Creates permanent download links in [Releases](https://github.com/YOUR_USERNAME/FRANVPN/releases)

---

## 📊 Build Status

| Component | Status |
|-----------|--------|
| Workflow file | ✅ Created |
| Documentation | ✅ Complete |
| Download guide | ✅ Updated |
| Ready to use | ✅ Yes |

---

## 📁 What Was Added

```
.github/
├── workflows/
│   └── build-apk.yml          ← Automated build workflow
└── CI_CD_GUIDE.md             ← Full documentation

DOWNLOAD_APK.md                 ← Updated with CI/CD info
GITHUB_ACTIONS_SETUP.md         ← This guide
```

---

## 🎯 How It Works

1. Push code to `main` branch
2. GitHub Actions automatically builds APK
3. Download from Actions (temporary) or Releases (permanent)

**Build takes:** ~5-8 minutes  
**Artifact storage:** 30 days  
**Release storage:** Forever  
**Cost:** FREE  

---

## 💾 Download Methods

### Latest Build
```
GitHub Actions → Build APK → Artifacts → Download
```
- Available: 30 days
- When: Immediately after build

### Stable Release
```
GitHub Releases → v1.0.0 → Assets → Download
```
- Available: Forever
- When: After creating git tag

---

## 📖 Documentation

- **[CI/CD Guide](.github/CI_CD_GUIDE.md)** — Complete workflow documentation
- **[Download Guide](DOWNLOAD_APK.md)** — How to get the APK
- **[Build Guide](APK_BUILD_GUIDE.md)** — Local build instructions

---

## ✨ Features

✅ Auto-build on every push  
✅ Auto-build on pull requests  
✅ Manual trigger available  
✅ Debug + Release APKs  
✅ Automatic releases on tags  
✅ Code quality checks  
✅ Artifact management  
✅ Zero cost (GitHub free tier)  

---

## 🚀 Ready to Build?

```bash
git push origin main
```

Then visit: https://github.com/YOUR_USERNAME/FRANVPN/actions

---

**Questions?** See the [full CI/CD Guide](.github/CI_CD_GUIDE.md)
