# 🎬 PhimHay — Android Movie App (Kotlin + KKPhim API)

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="96" alt="PhimHay Icon">
</p>

<p align="center">
  <a href="../../actions/workflows/android-release.yml">
    <img alt="Build" src="https://github.com/YOUR_USERNAME/PhimHay/actions/workflows/android-release.yml/badge.svg">
  </a>
  <img alt="Min SDK" src="https://img.shields.io/badge/minSdk-24-green">
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.9-blueviolet">
</p>

Ứng dụng xem phim Android viết bằng Kotlin, dùng API [phimapi.com](https://phimapi.com) (KKPhim).
Giao diện **Dark Cinema** — đen tối, accent đỏ, sang trọng.

---

## 🚀 Cài đặt (Lần đầu)

### 1. Clone và setup

```bash
git clone https://github.com/YOUR_USERNAME/PhimHay.git
cd PhimHay
bash scripts/setup.sh
```

Script `setup.sh` sẽ tự động:
- Download `gradle/wrapper/gradle-wrapper.jar` từ Gradle chính thức
- Verify/tạo keystore `app/phimhay-release.jks`
- In ra các giá trị cần thêm vào GitHub Secrets

### 2. Build local

```bash
./gradlew assembleDebug          # Debug APK
./gradlew assembleRelease        # Release APK (cần keystore)
./gradlew bundleRelease          # Release AAB
```

---

## 🔐 GitHub Actions — Cấu hình Secrets

Vào **repo Settings → Secrets and variables → Actions → New repository secret**:

| Secret name | Giá trị |
|---|---|
| `KEYSTORE_BASE64` | Output của `base64 -w0 app/phimhay-release.jks` |
| `KEYSTORE_PASS` | `phimhay123` |
| `KEY_ALIAS` | `phimhay` |
| `KEY_PASS` | `phimhay123` |

> ⚠️ **Quan trọng:** Đây là keystore demo. Với production app, hãy tạo keystore riêng với password mạnh hơn và **KHÔNG commit** vào git.

### Lấy KEYSTORE_BASE64

```bash
# macOS / Linux
base64 -w 0 app/phimhay-release.jks

# Windows
certutil -encode app\phimhay-release.jks tmp.b64 && type tmp.b64
```

---

## 🏷️ Release qua tag

```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions sẽ tự động build APK + AAB và tạo GitHub Release.

---

## 📁 Cấu trúc project

```
PhimHay/
├── .github/workflows/
│   └── android-release.yml   # CI/CD pipeline
├── app/
│   ├── phimhay-release.jks   # Keystore (demo — đổi cho production!)
│   ├── phimhay-release.jks.b64  # Base64 của keystore
│   ├── build.gradle
│   └── src/main/
│       ├── java/com/phimapp/
│       │   ├── model/        # Data classes
│       │   ├── network/      # Retrofit API
│       │   ├── data/         # Repository
│       │   ├── viewmodel/    # ViewModels
│       │   └── ui/           # Fragments
│       └── res/              # Layouts, drawables, icons
├── gradle/wrapper/
│   ├── gradle-wrapper.jar    # ← Cần chạy setup.sh để download
│   └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── scripts/
│   └── setup.sh              # First-time setup
└── build.gradle
```

---

## 🛠️ Tech Stack

| Thư viện | Version | Mục đích |
|---|---|---|
| Kotlin | 1.9.22 | Ngôn ngữ chính |
| Retrofit 2 | 2.9.0 | HTTP / REST API |
| OkHttp | 4.12.0 | HTTP client |
| Gson | 2.9.0 | JSON parsing |
| Glide | 4.16.0 | Image loading |
| ExoPlayer (Media3) | 1.2.1 | HLS video player |
| Navigation Component | 2.7.6 | Fragment navigation |
| ViewModel + LiveData | 2.7.0 | MVVM |
| FlexboxLayout | 3.0.0 | Episode grid |
| Material Design | 1.11.0 | UI components |

---

## 🎨 Design System

```
Background:  #0A0A0F  (đen đậm cinema)
Card:        #1A1A26  (dark navy)
Accent:      #FF4757  (đỏ cinematic)
Gold:        #FFD700  (số tập)
Purple:      #6C63FF  (phim bộ)
Teal:        #00D4AA  (phim lẻ)
```

---

## ⚠️ Lưu ý

- `gradle-wrapper.jar` **không được commit** do giới hạn host — chạy `setup.sh` để download
- Keystore demo chỉ dùng để test. Với production, tạo keystore mới và bảo mật cẩn thận
- App stream video từ nguồn bên ngoài, cần internet
