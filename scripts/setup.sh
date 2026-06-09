#!/bin/bash
# PhimHay - First-time setup script
# Run after cloning: bash scripts/setup.sh
set -e
echo "🎬 PhimHay Setup"
echo "================"

# 1. gradle-wrapper.jar
echo ""
echo "📦 Downloading gradle-wrapper.jar..."
mkdir -p gradle/wrapper
curl -fsSL \
  "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar" \
  -o gradle/wrapper/gradle-wrapper.jar && \
  echo "   ✅ Downloaded ($(wc -c < gradle/wrapper/gradle-wrapper.jar) bytes)" || \
  { echo "   ❌ Failed. Install Gradle and run: gradle wrapper --gradle-version 8.4"; exit 1; }

chmod +x gradlew
echo "   ✅ gradlew executable"

# 2. Keystore
echo ""
echo "🔑 Keystore..."
if [ -f "app/phimhay-release.jks" ]; then
  echo "   ✅ Found app/phimhay-release.jks"
else
  echo "   Generating keystore..."
  keytool -genkey -v \
    -keystore app/phimhay-release.jks -storetype JKS \
    -alias phimhay -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass phimhay123 -keypass phimhay123 \
    -dname "CN=PhimHay, OU=Mobile, O=PhimHay, L=HCM, S=HCM, C=VN"
  echo "   ✅ Keystore generated"
fi

# 3. Print secrets
echo ""
echo "🔐 Add to GitHub Secrets (repo Settings → Secrets → Actions):"
echo "─────────────────────────────────────────────────────────────"
echo "KEYSTORE_BASE64  →  $(base64 -w 0 app/phimhay-release.jks)"
echo "KEYSTORE_PASS    →  phimhay123"
echo "KEY_ALIAS        →  phimhay"
echo "KEY_PASS         →  phimhay123"
echo "─────────────────────────────────────────────────────────────"
echo ""
echo "✅ Setup done! Build: ./gradlew assembleDebug"
