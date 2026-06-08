# 🎬 PhimHay - Ứng dụng xem phim Android Kotlin

Ứng dụng xem phim Android được xây dựng với Kotlin, tích hợp API từ **phimapi.com** (KKPhim).

---

## ✨ Tính năng

- 🏠 **Trang chủ** — Banner phim nổi bật, phim mới cập nhật, phim bộ, phim lẻ
- 🔍 **Tìm kiếm** — Tìm phim theo tên/từ khóa realtime
- 📂 **Thể loại & Quốc gia** — Duyệt phim theo phân loại, hỗ trợ phân trang
- 🎬 **Chi tiết phim** — Poster, backdrop, thông tin đầy đủ, danh sách tập
- ▶️ **Trình phát video** — ExoPlayer hỗ trợ HLS (M3U8) & embed links
- 🌙 **Dark Cinema UI** — Giao diện đêm sang trọng, màu đỏ-đen

---

## 🛠️ Công nghệ

| Thư viện | Mục đích |
|---|---|
| Kotlin Coroutines | Async/non-blocking |
| Retrofit 2 + OkHttp | HTTP client & REST API |
| Gson | JSON parsing |
| Glide | Tải & cache ảnh |
| ExoPlayer (Media3) | Phát video HLS/M3U8 |
| Navigation Component | Điều hướng fragment |
| ViewModel + LiveData | MVVM architecture |
| Material Design 3 | UI components |

---

## 🏗️ Kiến trúc MVVM

```
com.phimapp/
├── model/          # Data classes (MovieItem, Episode, ...)
├── network/        # Retrofit API service
├── data/           # Repository pattern
├── viewmodel/      # ViewModels (Home, Detail, Search, Category)
└── ui/
    ├── home/       # HomeFragment
    ├── detail/     # MovieDetailFragment, PlayerFragment
    ├── search/     # SearchFragment
    └── category/   # CategoryFragment
```

---

## 🚀 Cài đặt & Chạy

### Yêu cầu
- Android Studio Hedgehog (2023.1.1) trở lên
- JDK 17
- Android SDK 34
- Gradle 8.2+

### Bước 1: Clone / Copy project
```bash
git clone <repo-url>
cd PhimApp
```

### Bước 2: Thêm FlexboxLayout dependency
Trong `app/build.gradle`, thêm:
```gradle
implementation 'com.google.android.flexbox:flexbox:3.0.0'
```

> **Lưu ý:** FlexboxLayout được dùng trong layout tập phim (episodeGrid).
> Nếu không muốn dùng, thay bằng `FlowLayout` hoặc `WrapLinearLayout` tùy chỉnh.

### Bước 3: Sync & Build
```
File → Sync Project with Gradle Files
Build → Make Project
```

### Bước 4: Chạy trên máy ảo/thật
- Mininum SDK: Android 7.0 (API 24)
- Target: Android 14 (API 34)

---

## 📱 Giao diện

### Palette màu sắc
```
bg_primary:    #0A0A0F  (nền chính đen đậm)
bg_card:       #1A1A26  (card tối)
accent_primary: #FF4757 (đỏ cinematic)
accent_gold:   #FFD700  (vàng cho số tập)
tag_fhd:       #6C63FF  (tím cho FHD/phim bộ)
tag_hd:        #00D4AA  (xanh ngọc cho HD)
```

### Screens
1. **HomeFragment** — Carousel banner ngang + Grid 3 cột + Horizontal scrolls
2. **SearchFragment** — Search bar + Grid kết quả
3. **MovieDetailFragment** — Hero backdrop + poster + thông tin đầy đủ + tập phim
4. **PlayerFragment** — Fullscreen ExoPlayer với HLS support
5. **CategoryFragment** — Grid phim + infinite scroll

---

## 🌐 API Endpoints sử dụng

| Endpoint | Mô tả |
|---|---|
| `GET /danh-sach/phim-moi-cap-nhat-v3?page=1` | Phim mới |
| `GET /phim/{slug}` | Chi tiết + tập phim |
| `GET /v1/api/danh-sach/{type}?page=1` | Danh sách theo loại |
| `GET /v1/api/tim-kiem?keyword=...` | Tìm kiếm |
| `GET /the-loai` | Danh sách thể loại |
| `GET /quoc-gia` | Danh sách quốc gia |
| `GET /v1/api/the-loai/{slug}` | Phim theo thể loại |
| `GET /v1/api/quoc-gia/{slug}` | Phim theo quốc gia |

Base URL: `https://phimapi.com/`

---

## 📝 Ghi chú

- App **không lưu trữ** bất kỳ video nào — chỉ stream từ nguồn bên ngoài
- Cần **kết nối internet** để hoạt động
- Một số phim dùng embed link (WebView) thay vì M3U8 — cần thêm WebViewFragment nếu muốn hỗ trợ đầy đủ

---

## 🔧 Mở rộng thêm

- [ ] Thêm WebView player cho embed links
- [ ] Lịch sử xem phim (Room Database)
- [ ] Danh sách yêu thích
- [ ] Download phim offline (nếu API hỗ trợ)
- [ ] Chế độ ngang (Landscape) cho player
- [ ] Phụ đề (Subtitle support)
- [ ] Cast to TV (Chromecast)
