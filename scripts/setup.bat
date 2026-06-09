@echo off
echo PhimHay Setup
echo =============

echo Downloading gradle-wrapper.jar...
mkdir gradle\wrapper 2>nul
curl -fsSL "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar" -o gradle\wrapper\gradle-wrapper.jar
echo Done.

echo.
echo Add these to GitHub Secrets:
echo KEYSTORE_PASS  = phimhay123
echo KEY_ALIAS      = phimhay
echo KEY_PASS       = phimhay123
echo KEYSTORE_BASE64 = (run: certutil -encode app\phimhay-release.jks tmp.b64 ^& type tmp.b64)
